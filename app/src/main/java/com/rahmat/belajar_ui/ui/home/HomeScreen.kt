
package com.rahmat.belajar_ui.ui.home

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rahmat.belajar_ui.data.model.Transaction
import com.rahmat.belajar_ui.presentation.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onLogout: () -> Unit
) {
    val context = LocalContext.current
    val user = viewModel.user
    val wallet = viewModel.wallet
    val isLoading = viewModel.isLoading
    val errorMessage = viewModel.errorMessage
    val topUpSuccess = viewModel.topUpSuccess
    val transferSuccess = viewModel.transferSuccess

    var showTopUpDialog by remember { mutableStateOf(false) }
    var showTransferDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.fetchData()
    }

    LaunchedEffect(topUpSuccess) {
        if (topUpSuccess) {
            Toast.makeText(context, "Top-up successful", Toast.LENGTH_SHORT).show()
            viewModel.resetState()
        }
    }

    LaunchedEffect(transferSuccess) {
        if (transferSuccess) {
            Toast.makeText(context, "Transfer successful", Toast.LENGTH_SHORT).show()
            viewModel.resetState()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Wallet") },
                actions = {
                    IconButton(onClick = {
                        viewModel.logout()
                        onLogout()
                    }) {
                        Icon(imageVector = Icons.Default.ExitToApp, contentDescription = "Logout")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else if (errorMessage != null) {
                Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
            } else {
                user?.let {
                    Column {
                        BalanceCard(balance = wallet?.balance ?: 0.0)
                        Spacer(modifier = Modifier.height(24.dp))
                        QuickActions(onTopUp = { showTopUpDialog = true }, onTransfer = { showTransferDialog = true })
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(text = "Recent Transactions", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        wallet?.let {
                            LazyColumn {
                                items(it.transactions) { transaction ->
                                    TransactionItem(transaction = transaction)
                                }
                            }
                        }
                    }
                }
            }
        }

        if (showTopUpDialog) {
            TopUpDialog(
                onDismiss = { showTopUpDialog = false },
                onConfirm = { amount ->
                    viewModel.topUp(amount)
                    showTopUpDialog = false
                }
            )
        }

        if (showTransferDialog) {
            TransferDialog(
                onDismiss = { showTransferDialog = false },
                onConfirm = { email, amount ->
                    viewModel.transfer(email, amount)
                    showTransferDialog = false
                }
            )
        }
    }
}

@Composable
fun BalanceCard(balance: Double) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            Text("Total Balance", fontSize = 16.sp, color = Color.White)
            Text("Rp ${String.format("%,.2f", balance)}", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}

@Composable
fun QuickActions(onTopUp: () -> Unit, onTransfer: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        QuickActionItem(icon = Icons.Default.ArrowUpward, text = "Send", onClick = onTransfer)
        QuickActionItem(icon = Icons.Default.Add, text = "Top Up", onClick = onTopUp)
        QuickActionItem(icon = Icons.Default.Receipt, text = "History", onClick = { /* TODO */ })
    }
}

@Composable
fun QuickActionItem(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer)
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = text, tint = MaterialTheme.colorScheme.onPrimaryContainer)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = text, fontSize = 12.sp)
    }
}

@Composable
fun TransactionItem(transaction: Transaction) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        ListItem(
            headlineContent = { Text(text = transaction.description, fontWeight = FontWeight.SemiBold) },
            supportingContent = { Text(text = transaction.created_at) },
            leadingContent = {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(if (transaction.type == "TOP_UP") Color(0xFFE8F5E9) else Color(0xFFFBE9E7)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (transaction.type == "TOP_UP") Icons.Default.ArrowDownward else Icons.Default.ArrowUpward,
                        contentDescription = transaction.type,
                        tint = if (transaction.type == "TOP_UP") Color(0xFF388E3C) else Color(0xFFD84315)
                    )
                }
            },
            trailingContent = {
                Text(
                    text = "Rp ${String.format("%,.2f", transaction.amount)}",
                    color = if (transaction.type == "TOP_UP") Color(0xFF388E3C) else Color(0xFFD84315),
                    fontWeight = FontWeight.Bold
                )
            }
        )
    }
}

@Composable
fun TopUpDialog(
    onDismiss: () -> Unit,
    onConfirm: (Double) -> Unit
) {
    var amount by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Top-up") },
        text = {
            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text("Amount") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        },
        confirmButton = {
            Button(onClick = { onConfirm(amount.toDouble()) }) {
                Text("Confirm")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun TransferDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, Double) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Transfer") },
        text = {
            Column {
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Amount") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(email, amount.toDouble()) }) {
                Text("Confirm")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
