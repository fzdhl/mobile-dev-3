package com.example.ticketapp2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ticketapp2.ui.theme.TicketApp2Theme
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                TicketApp()
            }
        }
    }
}

@Composable
fun TicketApp() {

    var ticketPrice by remember { mutableStateOf(50_000) }
    var ticketQuantity by remember { mutableStateOf(1) }
    var buyerName by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("Silakan pesan tiket") }
    var isProcessing by remember { mutableStateOf(false) }

    LaunchedEffect(isProcessing) {
        if (isProcessing) {
            delay(5000.milliseconds)
            status = "Tiket telah dipesan"
            isProcessing = false
        }
    }

    TicketScreen(
        ticketPrice = ticketPrice,
        ticketQuantity = ticketQuantity,
        buyerName = buyerName,
        status = status,
        isProcessing = isProcessing,
        onNameChange = {
            buyerName = it
        },
        onDecreaseQuantity = {
            if (ticketQuantity > 1) {
                ticketQuantity--
            }
        },
        onIncreaseQuantity = {
            ticketQuantity++
        },
        onOrderClick = {
            if (buyerName.isBlank()) {
                status = "Nama masih kosong"
            } else {
                status = "Memproses pesanan..."
                isProcessing = true
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketScreen(
    ticketPrice: Int,
    ticketQuantity: Int,
    buyerName: String,
    status: String,
    isProcessing: Boolean,

    onNameChange: (String) -> Unit,
    onDecreaseQuantity: () -> Unit,
    onIncreaseQuantity: () -> Unit,
    onOrderClick: () -> Unit
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Pemesanan Tiket",
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "Nama Pembeli",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = buyerName,
                onValueChange = onNameChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text("Masukkan nama Anda")
                },
                singleLine = true
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Harga Tiket",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Rp ${ticketPrice}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Jumlah Tiket",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = onDecreaseQuantity,
                    enabled = !isProcessing,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("-")
                }
                Text(
                    text = "$ticketQuantity",
                    modifier = Modifier.padding(horizontal = 30.dp),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Button(
                    onClick = onIncreaseQuantity,
                    enabled = !isProcessing,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("+")
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            val totalPrice = ticketPrice * ticketQuantity
            Text(
                text = "Total: Rp $totalPrice",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = onOrderClick,
                enabled = !isProcessing,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = if (isProcessing) {
                        "Memproses..."
                    } else {
                        "Pesan Tiket"
                    },
                    fontSize = 16.sp
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            val statusColor = when {
                status == "Nama masih kosong" -> Color(0xFFFDE7E9)
                status == "Tiket telah dipesan" -> Color(0xFFE8F5E9)
                status == "Memproses pesanan..." -> Color(0xFFE3F2FD)
                else -> Color(0xFFF5F5F5)
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = statusColor,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(16.dp)
            ) {
                Text(
                    text = "Status:",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = status,
                    fontSize = 16.sp
                )
            }
        }
    }
}