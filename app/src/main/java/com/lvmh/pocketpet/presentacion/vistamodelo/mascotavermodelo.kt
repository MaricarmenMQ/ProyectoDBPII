package com.lvmh.pocketpet.presentacion.vistamodelo
// Librerías básicas de Android
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

// Modelos y repositorios
import com.financepet.domain.models.PetState
import com.financepet.domain.models.Challenge
import com.financepet.domain.repositories.PetRepository

// Casos de uso
import com.financepet.domain.usecases.pet.UpdatePetHealthUseCase
import com.financepet.domain.usecases.pet.CheckChallengeUseCase

// Corrutinas
import kotlinx.coroutines.launch

/**
 * PetViewModel
 * Controla la mascota virtual y su estado de salud
 */
class mascotavermodelo(
    private val petRepository: PetRepository,
    private val updatePetHealthUseCase: UpdatePetHealthUseCase,
    private val checkChallengeUseCase: CheckChallengeUseCase
) : ViewModel() {

    // Estado completo de la mascota
    private val _petState = MutableLiveData<PetState>()
    val petState: LiveData<PetState> = _petState

    // Nivel de salud (1 = bajo, 5 = excelente)
    private val _petHealth = MutableLiveData<Int>()
    val petHealth: LiveData<Int> = _petHealth

    // Retos pendientes
    private val _hasPendingChallenges = MutableLiveData<Boolean>()
    val hasPendingChallenges: LiveData<Boolean> = _hasPendingChallenges

    // Lista de retos activos
    private val _activeChallenges = MutableLiveData<List<Challenge>>()
    val activeChallenges: LiveData<List<Challenge>> = _activeChallenges

    // Mensaje motivacional
    private val _motivationalMessage = MutableLiveData<String>()
    val motivationalMessage: LiveData<String> = _motivationalMessage

    // Carga el estado de la mascota
    fun loadPetState() {
        viewModelScope.launch {
            try {
                val state = petRepository.getCurrentPetState()
                _petState.value = state
                _petHealth.value = state.healthLevel
                loadPendingChallenges()
            } catch (e: Exception) {
                _petHealth.value = 3
                _hasPendingChallenges.value = false
            }
        }
    }

    // Carga los retos activos
    private suspend fun loadPendingChallenges() {
        try {
            val challenges = petRepository.getActiveChallenges()
            _activeChallenges.value = challenges
            _hasPendingChallenges.value = challenges.any { !it.isCompleted }
        } catch (e: Exception) {
            _activeChallenges.value = emptyList()
            _hasPendingChallenges.value = false
        }
    }

    // Actualiza la salud según el comportamiento financiero
    fun updatePetHealth(savingsRate: Double, budgetCompliance: Boolean, hasDebts: Boolean) {
        viewModelScope.launch {
            try {
                val newHealth = updatePetHealthUseCase.execute(
                    savingsRate = savingsRate,
                    budgetCompliance = budgetCompliance,
                    hasDebts = hasDebts
                )
                _petHealth.value = newHealth
                petRepository.updateHealthLevel(newHealth)
            } catch (e: Exception) {}
        }
    }

    // Verifica y actualiza los retos
    fun checkChallenges() {
        viewModelScope.launch {
            try {
                val challenges = _activeChallenges.value ?: return@launch
                for (c in challenges) {
                    if (!c.isCompleted && checkChallengeUseCase.execute(c)) {
                        petRepository.markChallengeAsCompleted(c.id)
                    }
                }
                loadPendingChallenges()
            } catch (e: Exception) {}
        }
    }

    // Alimenta a la mascota (sube su salud)
    fun feedPet() {
        viewModelScope.launch {
            try {
                val health = _petHealth.value ?: 3
                val newHealth = minOf(health + 1, 5)
                _petHealth.value = newHealth
                petRepository.updateHealthLevel(newHealth)
                petRepository.updateLastFeedDate()
            } catch (e: Exception) {}
        }
    }

    // Devuelve un mensaje motivacional según la salud
    fun getMotivationalMessage(health: Int): String {
        return when (health) {
            1 -> "😰 ¡Cuidado! Tus finanzas están mal."
            2 -> "😟 Estás en alerta, controla tus gastos."
            3 -> "😐 Vas bien, sigue así."
            4 -> "🙂 ¡Excelente! Estás saludable."
            5 -> "✨ ¡Perfecto! Estás prosperando."
            else -> "Monitorea tus finanzas."
        }
    }

    // Devuelve el nombre de la animación Lottie según salud
    fun getPetAnimationName(health: Int): String {
        return when (health) {
            1 -> "pet_critical.json"
            2 -> "pet_alert.json"
            3 -> "pet_stable.json"
            4 -> "pet_healthy.json"
            5 -> "pet_prosperous.json"
            else -> "pet_stable.json"
        }
    }
}
