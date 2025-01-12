package com.example.myapplication.repository

import com.example.myapplication.model.CriptoCurrency
import com.example.myapplication.model.CriptoResponse
import com.example.myapplication.model.SelectCriptoResponse
import com.example.myapplication.network.NetWorkLocalData
import com.example.myapplication.network.NetwokDataSource
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class BitsoRepositoryImpl @Inject constructor(private val netwokrDataSource: NetwokDataSource, private val netWorkLocalData :NetWorkLocalData) :
    BitsoRepository {

    override suspend fun loadCripto(): CriptoResponse = netwokrDataSource.loadCriptoCurrency()
    override suspend fun loadCriptoList(): List<CriptoCurrency> = netWorkLocalData.loadCriptoCurrency()
    override suspend fun saveDataList(data: List<CriptoCurrency>) {
        netWorkLocalData.saveCripto(data)
    }

    override fun loadSelectCriptoCurrency(idBook: String): Single<SelectCriptoResponse> = netwokrDataSource.getSelectCripto(id = idBook)
}