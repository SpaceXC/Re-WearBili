package cn.spacexc.wearbili.remake.app.player.cast.discover

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.URI
import java.util.UUID

class DeviceDiscoverViewModel : ViewModel() {
    private val client = HttpClient(CIO)

    data class Device(
        val uuid: UUID = UUID.randomUUID(),
        val descriptionLocation: String,
        val ipAddress: String,
        val friendlyName: String,
        val isBiliDevice: Boolean
    )

    var deviceList by mutableStateOf(listOf(Device(descriptionLocation = "", ipAddress = "", friendlyName = "", isBiliDevice = true)))
        private set


    suspend fun discoverDevices() {
        val tempDevices = deviceList.toMutableList()
        withContext(Dispatchers.IO) {
            val searchMessage = buildSSDPSearchMessage()

            val socket = DatagramSocket()
            socket.broadcast = true

            try {
                val address = InetAddress.getByName("239.255.255.250")

                while (true) { // Shorten the number of search iterations
                    val packet = DatagramPacket(
                        searchMessage.toByteArray(),
                        searchMessage.length,
                        address,
                        1900
                    )
                    socket.send(packet)


                    // Process SSDP responses
                    val responseBuffer = ByteArray(4096)
                    val responsePacket = DatagramPacket(responseBuffer, responseBuffer.size)

                    while (true) {
                        // Shorten the timeout for receiving responses
                        socket.soTimeout = 1000 // Adjust timeout as needed

                        try {
                            socket.receive(responsePacket)
                        } catch (e: Exception) {
                            break // Break out of the loop on timeout
                        }

                        val responseData = String(responsePacket.data, 0, responsePacket.length)

                        val location = responseData.split("\n")
                            .find { it.startsWith("Location:", ignoreCase = true) }
                            ?.replace("Location: ", "", ignoreCase = true)

                        location?.let {

                            if (tempDevices.find { device -> device.descriptionLocation == location } == null) {
                                val device = createDeviceByDescriptorXml(client, location)

                                //println("New device: ${device.friendlyName} $it")

                                tempDevices.add(device)
                            }
                        }
                    }

                    // Check for offline devices
                    val offlineDevices = buildList {
                        deviceList.forEach { device ->
                            if (!device.isOnline(client)) {
                                add(device)
                            }
                        }
                    }

                    tempDevices.removeAll(offlineDevices)

                    deviceList = tempDevices

                    deviceList.forEach {
                        //println(it.descriptionLocation)
                    }
                    println()
                    delay(1000)
                }
            } /*catch (e: Exception) {
            println("Error during SSDP discovery: ${e.message}")
            //discoverDevices()
        }*/ finally {
                socket.close()
            }
        }
    }

    private suspend fun Device.isOnline(client: HttpClient): Boolean {
        if(descriptionLocation.isEmpty()) return true
        return try {
            val response = client.get(descriptionLocation)
            response.status == HttpStatusCode.OK
        } catch (e: Exception) {
            false
        }
    }


    private suspend fun createDeviceByDescriptorXml(
        client: HttpClient,
        xmlLocation: String
    ): Device {
        val content = client.get(xmlLocation)
        val document = Jsoup.parse(content.bodyAsText())
        val friendlyName = document.select("device").select("friendlyName").text()
        val isBilibili = document.select("device").select("manufacturer").text() == "Bilibili Inc."
        return Device(
            descriptionLocation = xmlLocation,
            friendlyName = friendlyName,
            isBiliDevice = isBilibili,
            ipAddress = URI(xmlLocation.replace("\r", "").replace("\n", "")).host
        )
    }

    private fun buildSSDPSearchMessage(): String {
        return "M-SEARCH * HTTP/1.1\r\n" +
                "HOST: 239.255.255.250:1900\r\n" +
                "MAN: \"ssdp:discover\"\r\n" +
                "MX: 8\r\n" +
                "ST: urn:schemas-upnp-org:device:MediaRenderer:1\r\n" +
                "\r\n"
    }

    override fun onCleared() {
        super.onCleared()
        client.close()
    }
}