package com.example.myapplication.useCases

import com.example.myapplication.model.CriptoCurrency
import com.example.myapplication.repository.BitsoRepository
import javax.inject.Inject

/**
 * Created by: Juan Antonio Amado
 * date: 28,septiembre,2022
 */
class LoadLocalCriptoCurrencyUseCase @Inject constructor(private val repository: BitsoRepository) {

    suspend operator fun invoke(): List<CriptoCurrency> = repository.loadCriptoList()

    suspend operator fun invoke(list: List<CriptoCurrency>) = repository.saveDataList(list)


}