package com.example.naviku_versi_karisma.ui.kode_pribadi

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.test.core.app.ApplicationProvider
import com.example.naviku_versi_karisma.data.CodeRepository
import com.example.naviku_versi_karisma.data.local.Code
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class PersonalCodeListViewModelTest {

    @Mock
    private lateinit var codeRepository: CodeRepository

    @Mock
    private lateinit var codeLiveData: LiveData<List<Code>>

    @Mock
    private lateinit var observer: Observer<List<Code>>

    private lateinit var viewModel: PersonalCodeListViewModel

    @Before
    fun setUp() {
        Mockito.`when`(codeRepository.getAllCodes()).thenReturn(codeLiveData)
        viewModel = PersonalCodeListViewModel(getApplication())
    }

    @Test
    fun testGetAllCodes() {
        val codeList = listOf(Code(1, "Code 1"), Code(2, "Code 2"))
        Mockito.`when`(codeLiveData.value).thenReturn(codeList)

        viewModel.getAllCodes().observeForever(observer)

        Mockito.verify(codeRepository).getAllCodes()
        Mockito.verify(observer).onChanged(codeList)
    }

    private fun getApplication(): Application {
        val application = ApplicationProvider.getApplicationContext<Application>()
        return application
    }
}