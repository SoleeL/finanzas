package com.soleel.ui.template

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.soleel.ui.R

@Preview
@Composable
fun TransactionCreateTopAppBarPreview() {
    TransactionCreateTopAppBar(
        subTitle = R.string.payment_account_type_top_app_bar_subtitle
    ) {}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionCreateTopAppBar(
    subTitle: Int? = null,
    onClick: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = {
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally,
                content = {
                    Text(
                        text = stringResource(id = R.string.trasaction_create_title),
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )

                    if (null != subTitle) {
                        Text(
                            text = stringResource(id = subTitle),
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleSmall
                        )
                    }
                }
            )
        },
        navigationIcon = {
            IconButton(
                onClick = { onClick() },
                content = {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Volver a la pantalla principal",
                    )
                }
            )
        }
    )
}