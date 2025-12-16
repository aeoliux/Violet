package com.github.aeoliux.app.content.grades

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Comment
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Grade
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.aeoliux.app.content.prettyFormatted
import com.github.aeoliux.repositories.fill
import com.github.aeoliux.repositories.trimToTheLimit
import com.github.aeoliux.storage.Grade

@Composable
fun GradeView(grade: Grade) {
    val data = listOf(
        Triple(Icons.Default.Grade, "Grade", grade.grade),
        Triple(Icons.Default.Person, "Added by", grade.addedBy),
        Triple(Icons.Default.Bookmarks, "Subject", grade.subject),
        Triple(Icons.Default.Category, "Category", grade.category),
        Triple(Icons.Default.Numbers, "Value to the average", "${grade.gradeValue.toString().trimToTheLimit().fill()} x ${grade.weight}"),
        Triple(Icons.Default.CalendarToday, "Added at", grade.addDate.prettyFormatted())
    )

    LazyColumn {
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                data.forEachIndexed { index, (icon, label, info) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 60.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Spacer(Modifier.width(15.dp))

                            Icon(
                                imageVector = icon,
                                contentDescription = label,
                                tint = MaterialTheme.colorScheme.onSurface
                            )

                            Spacer(Modifier.width(15.dp))

                            Text(
                                text = label
                            )
                        }

                        Text(
                            text = info,
                            modifier = Modifier
                                .padding(end = 15.dp)
                        )
                    }

                    if ((index < data.lastIndex && grade.comment == null) || grade.comment != null)
                        HorizontalDivider(
                            color = MaterialTheme.colorScheme.surfaceContainer,
                            thickness = 3.dp
                        )
                }

                grade.comment?.let { comment ->
                    Column {
                        Spacer(Modifier.height(15.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Spacer(Modifier.width(15.dp))

                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Comment,
                                contentDescription = "Comment",
                                tint = MaterialTheme.colorScheme.primary
                            )

                            Spacer(Modifier.width(15.dp))

                            Text(
                                text = "Comment"
                            )
                        }

                        Text(
                            modifier = Modifier
                                .padding(15.dp),
                            text = comment
                        )
                    }
                }
            }
        }

        item {
            Spacer(Modifier.height(25.dp))
        }
    }
}