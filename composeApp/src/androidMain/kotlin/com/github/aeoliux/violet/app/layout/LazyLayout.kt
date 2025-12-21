package com.github.aeoliux.violet.app.layout

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.github.aeoliux.violet.app.components.ShapeBoxComposable

@Composable
fun LazyLayout(
    header: String? = null,
    isRefreshing: Boolean? = null,
    onRefresh: (() -> Unit)? = null,
    actions: (@Composable BoxScope.() -> Unit)? = null,
    content: LazyListScope.() -> Unit
) {
    val list = @Composable {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            content()

            item {
                Spacer(Modifier.height(
                    if (actions == null)
                        25.dp
                    else
                        150.dp
                ))
            }
        }
    }

    if (isRefreshing != null && onRefresh != null)
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = onRefresh,
            modifier = Modifier
                .fillMaxSize()
        ) {
            list()

            actions?.let { it() }
        }
    else
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            list()

            actions?.let { it() }
        }
}

fun getListItemShape(index: Int, lastIndex: Int): RoundedCornerShape {
    val baseRadius = 20.dp
    val regularRadius = 4.dp

    return when {
        lastIndex == 0 -> RoundedCornerShape(baseRadius)
        index == 0 -> RoundedCornerShape(topStart = baseRadius, topEnd = baseRadius, bottomStart = regularRadius, bottomEnd = regularRadius)
        index == lastIndex -> RoundedCornerShape(bottomStart = baseRadius, bottomEnd = baseRadius, topStart = regularRadius, topEnd = regularRadius)
        else -> RoundedCornerShape(regularRadius)
    }
}

@Composable
fun SectionHeader(text: String) {
    Column {
        Spacer(Modifier.height(20.dp))

        Text(
            text = text,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .padding(start = 15.dp, bottom = 5.dp)
        )
    }
}

@Composable
fun SectionListItem(
    index: Int,
    lastIndex: Int,
    header: String,
    leading: (@Composable () -> Unit)? = null,
    trailing: (@Composable () -> Unit)? = null,
    subheaders: List<String>? = null,
    onClick: (() -> Unit)? = null
) {
    SectionListItemComposable(
        index = index,
        lastIndex = lastIndex,
        leading = leading,
        trailing = trailing,
        header = {
            Text(
                text = header,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        subheaders = subheaders?.let { subheaders -> {
            subheaders.forEach { Text(it) }
        } },
        onClick = onClick
    )
}

@Composable
fun SectionListItemComposable(
    index: Int,
    lastIndex: Int,
    header: @Composable () -> Unit,
    leading: (@Composable () -> Unit)? = null,
    trailing: (@Composable () -> Unit)? = null,
    subheaders: (@Composable () -> Unit)? = null,
    onClick: (() -> Unit)? = null
) {
    ListItem(
        leadingContent = leading,
        trailingContent = trailing,
        headlineContent = header,
        supportingContent = subheaders,
        modifier = (onClick?.let { Modifier.clickable { it() } } ?: Modifier)
            .clip(getListItemShape(index, lastIndex)),
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surfaceBright
        )
    )
}

@Composable
fun BottomAction(
    modifier: Modifier,
    shape: Shape,
    imageVector: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    containerColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.secondary,
    size: Dp = 70.dp
) {
    Box(
        modifier = modifier
            .padding(bottom = 15.dp)
    ) {
        ShapeBoxComposable(
            shape = shape,
            containerColor = containerColor,
            modifier = Modifier
                .height(size)
                .width(size)
                .align(Alignment.Center)
                .clickable { onClick() }
        ) {
            Icon(
                imageVector = imageVector,
                contentDescription = contentDescription,
                tint = contentColor,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(23.dp)
            )
        }
    }
}