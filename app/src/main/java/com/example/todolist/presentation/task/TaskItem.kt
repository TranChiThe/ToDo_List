package com.example.todolist.presentation.task

import android.widget.Toast
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.ExperimentalWearMaterialApi
import androidx.wear.compose.material.rememberSwipeableState
import androidx.wear.compose.material.swipeable
import com.example.todolist.domain.model.Task
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalWearMaterialApi::class)
@Composable
fun TaskItem(
    task: Task,
    modifier: Modifier = Modifier,
    onFavorite: () -> Unit,
    onCheckBox: () -> Unit,
    onClick: () -> Unit,
    onDelete: () -> Unit,
) {
    var isChecked by remember { mutableStateOf(task.status == "Done") }
    val maxLength = 18
    val displayTitle =
        if (task.title.length > maxLength) {
            "${task.title.substring(0, maxLength)}..."
        } else {
            task.title
        }
    var isFavorite by remember(task.id) { mutableStateOf(task.favorite) }
    val coroutineScope = rememberCoroutineScope()

    val currentTime = System.currentTimeMillis()
    val isExpired = task.endTime < currentTime

    val swipeableState = rememberSwipeableState(0)
    val anchors = mapOf(0f to 0, -300f to -1, 200f to 1)

    val density = LocalDensity.current
    val swipeOffset by animateDpAsState(targetValue = with(density) { swipeableState.offset.value.toDp() })

    var showDeleteDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(swipeableState.currentValue) {
        if (swipeableState.currentValue == 1) {
            showDeleteDialog = true
        }
    }

    Box(modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp, horizontal = 5.dp)) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .background(
                        Color.LightGray.copy(alpha = 0.8f),
                        shape = RoundedCornerShape(12.dp),
                    ).swipeable(
                        state = swipeableState,
                        anchors = anchors,
                        orientation = Orientation.Horizontal,
                    ).offset(x = swipeOffset),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Card(
                modifier =
                    Modifier
                        .weight(1f)
                        .height(80.dp)
                        .shadow(6.dp, RoundedCornerShape(12.dp))
                        .clickable(enabled = swipeableState.currentValue == 0) { onClick() },
                colors =
                    CardDefaults.cardColors(
                        containerColor = if (isChecked) Color(0xFFF0F0F0) else Color.White,
                    ),
            ) {
                Row(
                    modifier = Modifier.padding(12.dp).fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f),
                    ) {
                        Checkbox(
                            checked = isChecked,
                            onCheckedChange = {
                                isChecked = it
                                val newStatus = if (isChecked) "Done" else "Pending"
                                task.status = newStatus
                                task.updateAt = System.currentTimeMillis()
                                onCheckBox()
                            },
                            colors =
                                CheckboxDefaults.colors(
                                    checkedColor = MaterialTheme.colorScheme.primary,
                                    uncheckedColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                ),
                        )
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text(
                                text = displayTitle,
                                style =
                                    MaterialTheme.typography.bodyLarge.copy(
                                        fontSize = 18.sp,
                                        fontWeight = if (isChecked) FontWeight.Normal else FontWeight.Medium,
                                    ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                textDecoration = if (isChecked) TextDecoration.LineThrough else null,
                                color = if (isChecked) Color.Gray.copy(alpha = 0.7f) else Color.Black,
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.DateRange,
                                    contentDescription = "Start Time",
                                    tint = Color.Gray,
                                    modifier = Modifier.size(16.dp),
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = formatTimestamp(task.startTime),
                                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                                    color = Color.Gray,
                                    modifier = Modifier.padding(top = 2.dp),
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.Default.DateRange,
                                    contentDescription = "End Time",
                                    tint = if (isExpired) Color.Red else Color(0xFFFF9900),
                                    modifier = Modifier.size(16.dp),
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = formatTimestamp(task.endTime),
                                    style =
                                        MaterialTheme.typography.bodySmall.copy(
                                            fontSize = 12.sp,
                                            color = if (isExpired) Color.Red else Color.Gray,
                                        ),
                                    modifier = Modifier.padding(top = 2.dp),
                                )
                                if (isExpired) {
                                    Text(
                                        text = "Expired",
                                        style =
                                            MaterialTheme.typography.bodySmall.copy(
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.Red,
                                            ),
                                        modifier = Modifier.padding(start = 4.dp, top = 2.dp),
                                    )
                                }
                            }
                        }
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.width(IntrinsicSize.Max).padding(end = 8.dp),
                    ) {
                        Text(
                            text = if (isChecked) "Done" else "Pending",
                            style = MaterialTheme.typography.labelMedium.copy(fontSize = 14.sp),
                            color = if (isChecked) Color(0xFF4CAF50) else Color(0xFFF44336),
                            modifier = Modifier.padding(end = 8.dp),
                        )
                        IconButton(
                            onClick = {
                                isFavorite = !isFavorite
                                task.favorite = isFavorite
                                coroutineScope.launch { onFavorite() }
                            },
                            modifier = Modifier.size(36.dp),
                        ) {
                            Icon(
                                imageVector =
                                    if (isFavorite) {
                                        Icons.Default.Favorite
                                    } else {
                                        Icons.Default.FavoriteBorder
                                    },
                                contentDescription = "Favorite",
                                tint = if (isFavorite) MaterialTheme.colorScheme.error else Color.Gray,
                            )
                        }
                    }
                }
            }
        }

        Row(
            modifier = Modifier.height(80.dp).align(Alignment.CenterEnd).padding(end = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (swipeOffset < -50.dp) {
                IconButton(
                    onClick = {
                        onClick()
                        Toast.makeText(context, "Edit clicked", Toast.LENGTH_SHORT).show()
                        coroutineScope.launch { swipeableState.animateTo(0) }
                    },
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = Color.Blue,
                    )
                }
                IconButton(
                    onClick = {
                        showDeleteDialog = true
                        Toast.makeText(context, "Delete clicked", Toast.LENGTH_SHORT).show()
                    },
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Color.Red,
                    )
                }
            }
        }
        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = {
                    showDeleteDialog = false
                    coroutineScope.launch { swipeableState.animateTo(0) }
                },
                title = { Text("Confirm Deletion") },
                text = { Text("Are you sure you want to delete this task?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showDeleteDialog = false
                            onDelete()
                            coroutineScope.launch { swipeableState.animateTo(0) }
                        },
                    ) {
                        Text("Delete", color = Color.Red)
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showDeleteDialog = false
                            coroutineScope.launch { swipeableState.animateTo(0) }
                        },
                    ) {
                        Text("Cancel")
                    }
                },
            )
        }
    }
}

fun formatTimestamp(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd/MM", Locale.getDefault())
    return sdf.format(Date(timestamp))
}
