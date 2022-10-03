package com.example.myapplication.useCases

import com.example.myapplication.model.CriptoCurrency
import com.example.myapplication.repository.BitsoRepository
import javax.inject.Inject

/**
 * Created by: Juan Antonio Amado
 * date: 03,octubre,2022
 */
class SaveLocalCriptoCurrencyUseCase @Inject constructor(private val repository: BitsoRepository) {

    suspend operator fun invoke(list: List<CriptoCurrency>) = repository.saveDataList(list)
}