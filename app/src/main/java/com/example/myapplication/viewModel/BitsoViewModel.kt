package com.example.myapplication.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.api.RetroFitRxClient
import com.example.myapplication.model.AskAndBidResponse
import com.example.myapplication.model.CriptoCurrency
import com.example.myapplication.model.SelectCriptoResponse
import com.example.myapplication.repository.BitsoRepository
import com.example.myapplication.useCases.LoadAllCriptoCurrencyUseCase
import com.example.myapplication.useCases.LoadCriptoWithFilterCurrencyUseCase
import com.example.myapplication.useCases.LoadLocalCriptoCurrencyUseCase
import com.example.myapplication.useCases.SaveLocalCriptoCurrencyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * Created by: Juan Antonio Amado
 * date: 16,septiembre,2022
 */
@HiltViewModel
class BitsoViewModel @Inject constructor(
    private val loadCriptoWithFilterCurrencyUseCase: LoadCriptoWithFilterCurrencyUseCase,
    private val loadAllCriptoCurrencyUseCase: LoadAllCriptoCurrencyUseCase,
    private val loadLocalCriptoCurrencyUseCase: LoadLocalCriptoCurrencyUseCase,
    private val saveLocalCriptoCurrencyUseCase: SaveLocalCriptoCurrencyUseCase,
    private val repository: BitsoRepository
) :
    ViewModel() {
    val _moneyCripto = MutableStateFlow<List<CriptoCurrency>>(listOf())
    var moneyAllCripto: StateFlow<List<CriptoCurrency>>
        get() = _moneyCripto
        set(value) {
            moneyAllCripto = value
        }
    var selectMoneyCripto: MutableLiveData<SelectCriptoResponse?> = MutableLiveData()
    var askBidMoneyCripto: MutableLiveData<AskAndBidResponse?> = MutableLiveData()

    fun getSelectCriptoCurrency(): MutableLiveData<SelectCriptoResponse?> {
        return selectMoneyCripto
    }

    fun getAskBidCriptoCurrency(): MutableLiveData<AskAndBidResponse?> {
        return askBidMoneyCripto
    }


    fun consultFilterCriptoCurrency() {
        viewModelScope.launch {
            val result = loadCriptoWithFilterCurrencyUseCase()
            _moneyCripto.value = result
        }
    }

    fun consultAllcriptoCurrency() {
        viewModelScope.launch(Dispatchers.IO) {
            if (loadLocalCriptoCurrencyUseCase.invoke().isEmpty()) {
                val result = loadAllCriptoCurrencyUseCase()
                if (result.isNotEmpty()) {
                    saveLocalCriptoCurrencyUseCase.invoke(result)
                    _moneyCripto.value = result
                } else {
                    _moneyCripto.value = listOf()
                }

            } else {
                _moneyCripto.value = loadLocalCriptoCurrencyUseCase.invoke()
            }

        }
    }

    fun selectCriptoCurrency(id: String) {
        val compositeDisposable = CompositeDisposable()
        compositeDisposable.add(
            repository.loadSelectCriptoCurrency(idBook = id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe { onSuccess: SelectCriptoResponse?, onError: Throwable? ->
                    onSuccess?.let {
                        selectMoneyCripto.postValue(it)
                    }

                    onError?.let {
                        selectMoneyCripto.postValue(null)
                    }
                })
    }

    fun getAskAndBidsCurrency(id: String) {
        val compositeDisposable = CompositeDisposable()

        compositeDisposable.add(
            RetroFitRxClient.buildService2()
                .getAskAndBids(id = id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe { onSuccess: AskAndBidResponse?, onError: Throwable? ->
                    onSuccess?.let {
                        askBidMoneyCripto.postValue(it)
                    }

                    onError?.let {
                        selectMoneyCripto.postValue(null)
                    }
                }
        )
    }
}
