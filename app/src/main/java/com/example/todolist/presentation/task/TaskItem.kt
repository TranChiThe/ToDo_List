package com.example.todolist.presentation.task

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todolist.domain.model.Task
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TaskItem(
    task: Task,
    modifier: Modifier = Modifier,
    onFavorite: () -> Unit,
    onCheckBox: () -> Unit,
    onClick: () -> Unit
) {
    var isChecked by remember { mutableStateOf(task.status == "Done") }
    val maxLength = 18
    val isDone by remember { derivedStateOf { task.status == "Done" } }
    val displayTitle = if (task.title.length > maxLength) {
        "${task.title.substring(0, maxLength)}..."
    } else {
        task.title
    }
    var isFavorite by remember(task.id) { mutableStateOf(task.favorite) }
    val coroutineScope = rememberCoroutineScope()

    val currentTime = System.currentTimeMillis()
    val isExpired = task.endTime < currentTime

    LaunchedEffect(task.favorite) {
        isFavorite = task.favorite
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .shadow(6.dp, RoundedCornerShape(12.dp))
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (isDone) Color(0xFFF0F0F0) else Color.White
        )
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Checkbox và Title
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Column {
                    Checkbox(
                        checked = isChecked,
                        onCheckedChange = {
                            isChecked = it
                            val newStatus = if (isChecked) "Done" else "Pending"
                            task.status = newStatus
                            task.updateAt = System.currentTimeMillis()
                            onCheckBox()
                        },
                        colors = CheckboxDefaults.colors(
                            checkedColor = MaterialTheme.colorScheme.primary,
                            uncheckedColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    )
                }
                Column(
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(
                        text = displayTitle,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontSize = 18.sp,
                            fontWeight = if (isChecked) FontWeight.Normal else FontWeight.Medium
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textDecoration = if (isChecked) TextDecoration.LineThrough else null,
                        color = if (isChecked) Color.Gray.copy(alpha = 0.7f) else Color.Black
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Start Time",
                            tint = Color.Gray,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = formatTimestamp(task.startTime),
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                            color = Color.Gray,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))

                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "End Time",
                            tint = if (isExpired) Color.Red else Color(0xFFFF9900),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = formatTimestamp(task.endTime),
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 12.sp,
                                color = if (isExpired) Color.Red else Color.Gray
                            ),
                            modifier = Modifier.padding(top = 2.dp)
                        )


                    }

                }
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier
                    .width(IntrinsicSize.Max)
                    .padding(end = 8.dp)
            ) {
                Text(
                    text = if (isChecked) "Done" else "Pending",
                    style = MaterialTheme.typography.labelMedium.copy(fontSize = 14.sp),
                    color = if (isChecked) Color(0xFF4CAF50) else Color(0xFFF44336),
                    modifier = Modifier.padding(end = 8.dp)
                )
                IconButton(
                    onClick = {
                        isFavorite = !isFavorite
                        task.favorite = isFavorite
                        coroutineScope.launch {
                            onFavorite()
                        }
                    },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (isFavorite) MaterialTheme.colorScheme.error else Color.Gray
                    )
                }
            }
        }
    }
}

fun formatTimestamp(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd/MM", Locale.getDefault())
    return sdf.format(Date(timestamp))
}