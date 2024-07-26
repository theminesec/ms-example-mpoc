package com.theminesec.ui

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.theminesec.MineHades.EMV_APPLIST
import com.theminesec.MineHades.EMV_PARAM
import com.theminesec.MineHades.MPoCAPI
import com.theminesec.MineHades.Model.MPoCResult
import com.theminesec.MineHades.vo.MhdEmvTransactionDto
import com.theminesec.MineHades.vo.MhdEmvTransactionType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory
import java.nio.ByteBuffer
import kotlin.random.Random

class SdkViewModel(private val app: Application) : AndroidViewModel(app) {
    private val logger = LoggerFactory.getLogger("PAY")
    private val json = Json { encodeDefaults = true }
    private var amount: String = ""


    private val _messages: MutableStateFlow<List<String>> = MutableStateFlow(listOf())
    val messages: StateFlow<List<String>> = _messages
    private val licenseName = "MineSec_1.10.100T_am.mspayhub.com_mpoc_DEBUG.license"
    //"MineSec_ams.theminesec_898cd_DEBUG.license"

    private val _isSdkInitialized = MutableStateFlow<Boolean>(false)
    val isSdkInitialized = _isSdkInitialized.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _isDoingRegistration = MutableStateFlow(false)
    val isDoingRegistration = _isDoingRegistration.asStateFlow()

    fun writeMessage(message: String) = viewModelScope.launch {
        val dat = _messages.value.toMutableList().apply {
            add(message)
        }
        _messages.emit(dat)
    }

    fun clearLog() = viewModelScope.launch {
        _messages.emit(emptyList())
    }

    fun initSdk() = viewModelScope.launch {
        writeMessage("start init SDK")
        writeMessage("step 1. you should bind the service")
        onInitialization(true)
        MPoCAPI.initSdk(licenseName, app) { result ->
            when (result) {
                is MPoCResult.Success -> {
                    writeMessage("init result success")
                    val data = result.data
                    writeMessage(data.toString())
                    onInitialization(false)
                    signalInitDone(true)
                }

                is MPoCResult.Failure -> {
                    writeMessage("init fails " + result.code)
                    writeMessage("init fails message " + result.message)
                    writeMessage("init fail contextual " + result.contextual)

                    onInitialization(false)
                    signalInitDone(true)
                }

            }

        }
        writeMessage("step 2. call initMPoCSDK API to init SDK")
        writeMessage("register....")
    }


    fun registerSdk() = viewModelScope.launch {
        onRegisterStatus(true)
        MPoCAPI.registerSdk(false) {
            when (it) {
                is MPoCResult.Failure -> {
                    writeMessage("sdk registration fails ${it.code} ${it.message} context=${it.contextual}")
                    onRegisterStatus(false)
                }

                is MPoCResult.Success -> {
                    writeMessage("register success $it")
                    onRegisterStatus(false)
                }

            }
        }
    }


    private fun onInitialization(status: Boolean) = viewModelScope.launch {
        _isLoading.emit(status)
    }

    private fun signalInitDone(status: Boolean) = viewModelScope.launch {
        _isSdkInitialized.emit(status)
    }

    private fun onRegisterStatus(status: Boolean) = viewModelScope.launch {
        _isDoingRegistration.emit(status)
    }

    fun setupEmv() {
        writeMessage("setup EMV PARAM")
        setupEmvParam()

        writeMessage("setup EMV APP")
        setEmvApp()
    }

    @OptIn(ExperimentalStdlibApi::class)
    fun setupEmvParam() {
        val params = EMV_PARAM()
        params.merchName = ByteArray(64)
        "SoftPOS Merchant".encodeToByteArray().copyInto(params.merchName)
        params.terminalType = 0x21.toByte()
        params.merchCateCode = "5734".hexToByteArray()
        params.merchId = "0102030405060708090A0B0C0D0E0F".hexToByteArray()
        params.termId = "0102030405060708".hexToByteArray()
        params.capability = "0068C8".hexToByteArray()
        params.exCapability = "1200025000".hexToByteArray()
        params.transCurrExp = 0x02.toByte()
        params.referCurrExp = 0x02.toByte()
        params.referCurrCode = "0840".hexToByteArray()
        params.countryCode = "0840".hexToByteArray()
        params.transCurrCode = "0840".hexToByteArray()
        params.referCurrCode = "0840".hexToByteArray()
        params.referCurrCon = 1000.toLong()
//        params.setbBatchCapture(0x00.toByte())
//        params.setbSupportAdvices(0x00.toByte())
        params.transType = 0x00.toByte()
        params.forceOnline = 1
        params.getDataPIN = 0x00.toByte()
        params.surportPSESel = 0x01.toByte()
        params.cL_FloorLimit = 100000.toLong()
        params.cL_TransLimit = 100000.toLong()
        params.cL_CVMLimit = 20000.toLong()
        params.cL_ReaderCapability = "C8".hexToByteArray()
        params.cL_ReaderCapabilityEx = "18E00003".hexToByteArray()
        params.terminalFloorLimit = ByteBuffer.allocate(4).putInt(1000000).array()
        params.setbImplAcqOpt("0000".hexToByteArray())

        MPoCAPI.setupEmvParam(params)

    }


    @OptIn(ExperimentalStdlibApi::class)
    fun setEmvApp() {
        /**
         * {
         * 	"AID": "A000000003",
         * 	"AidLen": 5,
         * 	"SelFlag": 0,
         * 	"Priority": 0,
         * 	"TargetPer": 0,
         * 	"MaxTargetPer": 0,
         * 	"FloorLimitCheck": 1,
         * 	"RandTransSel": 1,
         * 	"VelocityCheck": 1,
         * 	"Threshold": 0,
         * 	"TACDenial": "0000000000",
         * 	"TACOnline": "0000000000",
         * 	"TACDefault": "0000000000",
         * 	"AcquierId": "000000123456",
         * 	"dDOL": "039F3704",
         * 	"tDOL": "0F9F02065F2A029A039C0195059F3704",
         * 	"Version": "0096",
         * 	"RiskManData": "00000000000000000000",
         * 	"EC_bTermLimitCheck": 1,
         * 	"EC_TermLimit": 1000,
         * 	"CL_bStatusCheck": 1,
         * 	"CL_FloorLimit": 0,
         * 	"CL_TransLimit": 50000,
         * 	"CL_CVMLimit": 1000,
         * 	"KernelId": 0,
         * 	"T_TTQ": "26804080"
         * }
         */
        val emvApp = EMV_APPLIST()
        emvApp.aid = "A000000003".hexToByteArray()
        emvApp.aidLen = 5.toByte()
        emvApp.selFlag = 0.toByte()
        emvApp.priority = 0.toByte()
        emvApp.targetPer = 0.toByte()
        emvApp.maxTargetPer = 0.toByte()
        emvApp.floorLimit = 1
        emvApp.randTransSel = 1
        emvApp.velocityCheck = 1
        emvApp.threshold = 0
        emvApp.tacDenial = "0000000000".hexToByteArray()
        emvApp.tacOnline = "0000000000".hexToByteArray()
        emvApp.tacDefault = "0000000000".hexToByteArray()
        emvApp.acquierId = "000000123489".hexToByteArray()
        emvApp.setdDOL("039F3704".hexToByteArray())
        emvApp.settDOL("0F9F02065F2A029A039C0195059F3704".hexToByteArray())
        emvApp.version = "0096".hexToByteArray()
        emvApp.riskManData = "00000000000000000000".hexToByteArray()
        emvApp.cL_bStatusCheck = 1
        emvApp.cL_FloorLimit = 0
        emvApp.cL_TransLimit = 50000
        emvApp.cL_CVMLimit = 1000
        emvApp.kernelId = 0
        emvApp.t_TTQ = "26804080".hexToByteArray()

        MPoCAPI.setupEmvApp(listOf(emvApp))

    }


    fun setTranAmount(it: String) {
        amount = it
        //writeMessage("trans amount $it")
    }

    @SuppressLint("LogNotTimber")
    fun submitTransaction() {
        if (amount.isEmpty()) {
            writeMessage("Transaction amount is not set")
            return
        }

        val dto = MhdEmvTransactionDto().apply {
            txnAmount = amount.toLong()
            cashBackAmount = amount.toLong()
            txnNo = Random.nextLong(999999)
            txnType = MhdEmvTransactionType.MHD_EMV_TRANS_PURCHASE
            cardKeyAlias = "keyAlias"
        }
        MPoCAPI.startEmvTransaction(dto) {
            when (it) {
                is MPoCResult.Success -> {
                    writeMessage("transaction success $it")
                }

                is MPoCResult.Failure -> {
                    writeMessage("transaction fails ${it.code} ${it.message} contextu=${it.contextual}")
                }
            }
        }
        writeMessage("start to do transaction")
    }

    fun pinEntry() {

        MPoCAPI.pinCapture("1122334455667788") {
            when (it) {
                is MPoCResult.Failure -> {
                    writeMessage("PIN Capture fails $it")
                }

                is MPoCResult.Success -> {
                    writeMessage("PIN Capture success: PIN BLOCK ${it.data.pinBlock}, PAN ${it.data.panLast4}, keyId ${it.data.pinKeyId}")

                }
            }
        }
    }


}
