package com.github.aeoliux.violet.app.content.grades

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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Comment
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.Grade
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.github.aeoliux.violet.app.content.prettyFormatted
import com.github.aeoliux.violet.app.layout.LazyLayout
import com.github.aeoliux.violet.app.layout.SectionHeader
import com.github.aeoliux.violet.app.layout.SectionListItem
import com.github.aeoliux.violet.repositories.fill
import com.github.aeoliux.violet.repositories.trimToTheLimit
import com.github.aeoliux.violet.storage.Grade

@Composable
fun GradeView(grade: Grade) {
    val data1 = listOf(
        Pair(
            first = "Info",
            second = listOf(
                Triple(Icons.Default.Grade, "Grade", grade.grade),
                Triple(Icons.Default.Person, "Added by", grade.addedBy),
                Triple(Icons.Default.Bookmarks, "Subject", grade.subject),
                Triple(Icons.Default.CalendarToday, "Added at", grade.addDate.prettyFormatted())
            )
        ),
        Pair(
            first = "Values",
            second = listOf(
                Triple(Icons.Default.Category, "Category", grade.category),
                Triple(
                    Icons.Default.Numbers,
                    "Value to the average",
                    "${grade.gradeValue.toString().trimToTheLimit().fill()} x ${grade.weight}"
                )
            )
        ),

        )

    val data = grade.comment?.let {
        data1.plus(
            Pair(
                first = "Additional information",
                second = listOf(
                    Triple(Icons.Default.Comment, "Comment", it)
                )
            )
        )
    } ?: data1

    LazyLayout {
        data.forEach { (label, content) ->
            item {
                SectionHeader(label)
            }

            itemsIndexed(content) { index, (icon, label, value) ->
                SectionListItem(
                    index = index,
                    lastIndex = content.lastIndex,

                    leading = {
                        Icon(
                            imageVector = icon,
                            contentDescription = label
                        )
                    },

                    header = label,
                    subheaders = listOf(value)
                )
            }
        }
    }
}