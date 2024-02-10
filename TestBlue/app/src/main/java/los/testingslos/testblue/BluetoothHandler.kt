package los.testingslos.testblue

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.le.ScanResult
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import com.welie.blessed.*
import com.welie.blessed.WriteType.SIGNED
import com.welie.blessed.WriteType.WITHOUT_RESPONSE
import com.welie.blessed.WriteType.WITH_RESPONSE
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception
import java.nio.ByteOrder
import java.security.cert.Extension
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.zip.CRC32
import java.util.zip.Checksum
import kotlin.experimental.xor


@SuppressLint("StaticFieldLeak")
object BluetoothHandler {

    private lateinit var context: Context
    lateinit var centralManager: BluetoothCentralManager
    private val handler = Handler(Looper.getMainLooper())
    private val measurementFlow_ = MutableStateFlow("Waiting for measurement")
    val measurementFlow = measurementFlow_.asStateFlow()
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private var peripheralCurrent : BluetoothPeripheral? = null

    // TODO:work with our service
    val SERVICE_EAR_ONE: UUID = UUID.fromString("00001801-0000-1000-8000-00805f9b34fb")
    val SERVICE_EAR_TWO: UUID = UUID.fromString("00001800-0000-1000-8000-00805f9b34fb")
    val SERVICE_EAR_THREE: UUID = UUID.fromString("0000fff0-0000-1000-8000-00805f9b34fb")
    val SERVICE_EAR_THR__ONE_CHARA : UUID = UUID.fromString( "0000fff1-0000-1000-8000-00805f9b34fb")
    val SERVICE_EAR_THR_TWO_CHARA  : UUID = UUID.fromString("000fff2-0000-1000-8000-00805f9b34fb")



    val VALUES_EAR_UUID : UUID = UUID.fromString("00002a00-0000-1000-8000-00805f9b34fb")
    val VALUES_EAR_UUID2 : UUID = UUID.fromString("00002a01-0000-1000-8000-00805f9b34fb")
    val VALUES_EAR_UUID3 : UUID = UUID.fromString("00002a04-0000-1000-8000-00805f9b34fb")


    private val bluetoothPeripheralCallback = object : BluetoothPeripheralCallback() {

        override fun onServicesDiscovered(peripheral: BluetoothPeripheral) {

            peripheral.requestConnectionPriority(ConnectionPriority.HIGH)

            //println(" discovered Peripheril S")



//            for ( a in peripheral.services){
//                println(" a == ${a.uuid}")
//                println(" b == ${a.characteristics.size}")
//                println(" c == ${a.describeContents()}")
//                println(" d == ${a.describeContents() }")
//
//                println(" e == ${a.includedServices.size} ")
//                println(" f == ${a.instanceId}")
//                println(" g == ${a.type}")
//
//                if(a.characteristics.size > 1){
//                    val aG = a.characteristics
//
//                    for (k in 0..<aG.size){
//                        println(" in b2 == ${aG[k].uuid}")
//                    }
//                    //println(" in b == ${aG.size} ")
//                    //println(" in b2 == ${aG[0].uuid}")
//                    //println(" in b22 == ${aG[1].uuid}")
//                }
//
//            }



            val byteOne = byteArrayOf(0x43, 0x4F, 0x4E)
             val calendar = Calendar.getInstance()
            calendar.timeZone = TimeZone.getTimeZone("UTC")

            println("my time zero = ${System.currentTimeMillis().toInt()}")

//             val bytesCalendar = BluetoothBytesBuilder(4u, ByteOrder.LITTLE_ENDIAN)
//                 .addInt32(System.currentTimeMillis().toInt())
//                .build()

            val data = System.currentTimeMillis().toInt()

            val buffer : ByteArray = ByteArray(4)

    buffer[0] = (data shr 0).toByte()
    buffer[1] = (data shr 8).toByte()
    buffer[2] = (data shr 16).toByte()
    buffer[3] = (data shr 24).toByte()



              println(" my ae  size = ${buffer.size}")
           val i1 = byteOne.plus(buffer)


            val byteTwo = byteArrayOf(
                 0x01,
                 0x00,
                 0x00,
                 0x00,
                 0x00,
                 0x00,
                 0x00,
                 0x00,
                 0x00,
                 0x00,
                 0x00,
                 0xB6.toByte(), // was 0xB6.toByte()
            )

            //byteOne.plus(byteTwo)
            val i2 = i1.plus(byteTwo)


            val checksum =  CRC32();
            checksum.update(i2, 0, i2.size)
            println(" my control summ Zero = ${checksum.value}")
            val checksumValue  = checksum.value.toByte()

            val bytesFinal = BluetoothBytesBuilder(1u, ByteOrder.LITTLE_ENDIAN)
                .addInt8(checksumValue)
                .build()

           val we = byteArrayOf(checksumValue)

            println(" my size = ${we.size}")
            val i4 = i2.plus(we)


            println(" my final byte size = ${i4.size}")




//    val VALUES_EAR_UUID
//    val VALUES_EAR_UUID2
//    val VALUES_EAR_UUID3

    // SERVICE_EAR_THR__ONE_CHARA
    // SERVICE_EAR_THR_TWO_CHARA
            peripheral.getCharacteristic(SERVICE_EAR_THREE, SERVICE_EAR_THR__ONE_CHARA)?.let {


                if(it.supportsReading()){
                    println(" ok reading ")
                }

                if(it.supportsWritingWithResponse()){
                    println(" support write tith respo ")
                    peripheral.writeCharacteristic(it, i4, WITH_RESPONSE)
                }

                if(it.supportsWritingWithoutResponse()){
                    println(" suppo weite with rRPS ")
                    peripheral.writeCharacteristic(it, i4, WITHOUT_RESPONSE)
                }
             }

             peripheral.readCharacteristic(SERVICE_EAR_THREE, SERVICE_EAR_THR__ONE_CHARA)

        }




        override fun onNotificationStateUpdate(peripheral: BluetoothPeripheral, characteristic: BluetoothGattCharacteristic, status: GattStatus) {

             println(" in OnNotificationStateUp")

        //            if (status == GattStatus.SUCCESS) {
//                val isNotifying = peripheral.isNotifying(characteristic)
//                Timber.i("SUCCESS: Notify set to '%s' for %s", isNotifying, characteristic.uuid)
//                if (characteristic.uuid == CONTOUR_CLOCK) {
//                    writeContourClock(peripheral)
//                } else if (characteristic.uuid == GLUCOSE_RECORD_ACCESS_POINT_CHARACTERISTIC_UUID) {
//                    writeGetAllGlucoseMeasurements(peripheral)
//                }
//            } else {
//                Timber.e("ERROR: Changing notification state failed for %s (%s)", characteristic.uuid, status)
//            }
        }




        override fun onCharacteristicUpdate(peripheral: BluetoothPeripheral, value: ByteArray, characteristic: BluetoothGattCharacteristic, status: GattStatus) {

             println(" updateLLOS character Update data")


            when (characteristic.uuid) {


                SERVICE_EAR_THR__ONE_CHARA -> {
                    println(" ok needed return is ")

                    //println( " my byte 0 == ${Integer.toBinaryString(value[0])}" )
                    //println( " my byte 0 == ${value[19]}" )

                    val parser = BluetoothBytesParser(value, 0, ByteOrder.LITTLE_ENDIAN)

                    try {

                        val flags = parser.getInt8()
                        val aName = parser.getInt8()
                        val bName = parser.getInt8()


                        println(" in this flags ${flags}") // C 067
                        println(" in this a ${aName}")     // O 079
                        println("in this b ${bName}")      // N 078


                        val time22 = parser.getInt32().toInt()
//                        val timeDetect1 = parser.getInt8()
//                        val timeDetect2 = parser.getInt8()
//                        val timeDetect3 = parser.getInt8()
//                        val timeDetect4 = parser.getInt8()
//  if (   timeDetect1 < 0  ) {
//       println(" error in read int 1")
//                        }
//
//                          if (   timeDetect2 < 0  ) {
//       println(" error in read int 2")
//                        }
//
//
//                          if (   timeDetect3 < 0  ) {
//       println(" error in read int 3")
//                        }
//
//                          if (   timeDetect4 < 0  ) {
//       println(" error in read int 4 ")
//                        }


                        println(" my time ${time22}")

                      //  println(" my time is 2 == ${ (timeDetect1 shl 24) + (timeDetect2 shl 16) + (timeDetect3 shl 8) + (timeDetect4 shl 0 )}")

                        val parN = parser.getInt8();
                        if(parN == 0x00){
                            println(" 0x00 is this ")
                        }else if(parN == 0x01) {
                             println(" 0x01 is this ")
                        }

                        val byte9 = parser.getInt16().toInt()
                        println(" my byte 9 == ${byte9}")
//                        val byt10 = parser.getInt8()
//
//                        if(byte9 < 0){
//                            println(" error in 9 ")
//                        }
//
//                        if(byt10 < 0){
//                            println(" error in 10 ")
//
//                        }

                            //println(" my battery charge  = ${ (byte9 shl 0) + (byt10 shl 8)}")
                        //val byte10 = parser.getInt8()
                          //                      println(" my battery2 charge  = ${byte10}")
                        val paketExten = parser.getUInt8().toInt()
                        println(" to much packet = ${paketExten}")

                        val paketStandart1 = parser.getInt8()
                        val paketStan2     = parser.getInt8()

                        println(" my number packet stan = ${(paketStandart1 shl 8) + (paketStan2 shl 0)}")

                        val a14 = parser.getInt8()
                        val a15 = parser.getInt8()
                        val a16 = parser.getInt8()
                        val a17 = parser.getInt8()
                        val a18 = parser.getInt8()
                        println(" pre 19 ")
                        val a19 = parser.getInt8()
                        println(" pre 20 ")
                        val a20 = parser.getInt8()
                        println(" my control sum == ${a20}")

                    }catch (Ex : Exception){
                         println(" error catch work")
                        return
                    }




                    val byteOne = byteArrayOf(0x43, 0x4F, 0x4E)
//             val calendar = Calendar.getInstance()
//             calendar.timeZone = TimeZone.getTimeZone("UTC")

             val bytesCalendar = BluetoothBytesBuilder(4u, ByteOrder.LITTLE_ENDIAN)
                 .addInt32(System.currentTimeMillis().toInt())
                .build()

           val i1 = byteOne.plus(bytesCalendar)


            val byteTwo = byteArrayOf(
                 0x01,
                 0x00,
                 0x01,
                 0x00,
                 0x00,
                 0x00,
                 0x00,
                 0x00,
                 0x00,
                 0x00,
                 0x00,
                 0x00,
            )

            //byteOne.plus(byteTwo)
            val i2 = i1.plus(byteTwo)


            val checksum =  CRC32();
            checksum.update(i2, 0, i2.size)
            //println(" my control summ Zero = ${checksum.value}")
            val checksumValue  = checksum.value.toByte()

            val bytesFinal = BluetoothBytesBuilder(1u, ByteOrder.LITTLE_ENDIAN)
                .addUInt8(checksumValue)
                .build()

            val i4New = i2.plus(bytesFinal)

               peripheral.writeCharacteristic(SERVICE_EAR_THREE, SERVICE_EAR_THR__ONE_CHARA, i4New, WITHOUT_RESPONSE)
             peripheral.writeCharacteristic(characteristic, i4New, WITHOUT_RESPONSE)



                }




            }
        }

        fun sendMeasurement(value: String) {
            scope.launch {
                Timber.i(value)
                measurementFlow_.emit(value)
            }
        }

        private fun writeContourClock(peripheral: BluetoothPeripheral) {
            val calendar = Calendar.getInstance()
            val offsetInMinutes = calendar.timeZone.rawOffset / 60000
            calendar.timeZone = TimeZone.getTimeZone("UTC")

            val bytes = BluetoothBytesBuilder(10u, ByteOrder.LITTLE_ENDIAN)
                .addUInt8(1u)
                .addUInt16(calendar[Calendar.YEAR])
                .addUInt8(calendar[Calendar.MONTH] + 1)
                .addUInt8(calendar[Calendar.DAY_OF_MONTH])
                .addUInt8(calendar[Calendar.HOUR_OF_DAY])
                .addUInt8(calendar[Calendar.MINUTE])
                .addUInt8(calendar[Calendar.SECOND])
                .addInt16(offsetInMinutes)
                .build()

            //peripheral.writeCharacteristic(CONTOUR_SERVICE_UUID, CONTOUR_CLOCK, bytes, WITH_RESPONSE)
        }

        private fun writeGetAllGlucoseMeasurements(peripheral: BluetoothPeripheral) {
            val opCodeReportStoredRecords: Byte = 1
            val operatorAllRecords: Byte = 1
            val command = byteArrayOf(opCodeReportStoredRecords, operatorAllRecords)
            //peripheral.writeCharacteristic(GLUCOSE_SERVICE_UUID, GLUCOSE_RECORD_ACCESS_POINT_CHARACTERISTIC_UUID, command, WITH_RESPONSE)
        }
    }

    private val bluetoothCentralManagerCallback = object : BluetoothCentralManagerCallback() {

        override fun onDiscovered(peripheral: BluetoothPeripheral, scanResult: ScanResult) {
            Timber.i("Found peripheral '${peripheral.name}' with RSSI ${scanResult.rssi} and address${peripheral.address}")


            //centralManager.cancelConnection(peripheral)


            if(peripheral.name == "EAR"){
                 centralManager.stopScan()

            if (peripheral.needsBonding() && peripheral.bondState == BondState.NONE) {
                // Create a bond immediately to avoid double pairing popups
                println(" create bond !")
                centralManager.createBond(peripheral, bluetoothPeripheralCallback)

            } else {
                println(" all connect ! ")
                centralManager.autoConnect(peripheral, bluetoothPeripheralCallback)
            }


                peripheralCurrent = peripheral
            }


        }



        override fun onConnected(peripheral: BluetoothPeripheral) {
            Timber.i("connected to '${peripheral.name}'")
            Toast.makeText(context, "Connected to ${peripheral.name}", LENGTH_SHORT).show()
        }

        override fun onDisconnected(peripheral: BluetoothPeripheral, status: HciStatus) {
            Timber.i("disconnected '${peripheral.name}'")
            Toast.makeText(context, "Disconnected ${peripheral.name}", LENGTH_SHORT).show()

            handler.postDelayed(
                { centralManager.autoConnect(peripheral, bluetoothPeripheralCallback) },
                15000
            )

        }

        override fun onConnectionFailed(peripheral: BluetoothPeripheral, status: HciStatus) {
            Timber.e("failed to connect to '${peripheral.name}'")
        }

        override fun onBluetoothAdapterStateChanged(state: Int) {
            Timber.i("bluetooth adapter changed state to %d", state)
            if (state == BluetoothAdapter.STATE_ON) {
                // Bluetooth is on now, start scanning again
                // Scan for peripherals with a certain service UUIDs
                centralManager.startPairingPopupHack()
                startScanning()
            }
        }
    }



    fun stopAndClose(){
       // cancelConnection

        //if( peripheralCurrent!!.){
            centralManager.cancelConnection(peripheralCurrent!!)
       // }

        centralManager.close()
    }




    fun startScanning() {
        if(centralManager.isNotScanning) {
            println(" scanScanning LL! ")

            centralManager.scanForPeripherals()
//            centralManager.scanForPeripheralsWithServices(
//                setOf(
//                    BLP_SERVICE_UUID,
//                    GLUCOSE_SERVICE_UUID,
//                    HRS_SERVICE_UUID,
//                    HTS_SERVICE_UUID,
//                    PLX_SERVICE_UUID,
//                    WSS_SERVICE_UUID
//                )
//            )
        }
    }

    fun initialize(context: Context) {
        Timber.plant(Timber.DebugTree())
        Timber.i("initializing BluetoothHandler")
        this.context = context.applicationContext
        this.centralManager = BluetoothCentralManager(this.context, bluetoothCentralManagerCallback, handler)
    }
}

// Peripheral extension to check if the peripheral needs to be bonded first
// This is application specific of course
fun BluetoothPeripheral.needsBonding(): Boolean {
    return name.startsWith("Contour") ||
            name.startsWith("A&D")
}