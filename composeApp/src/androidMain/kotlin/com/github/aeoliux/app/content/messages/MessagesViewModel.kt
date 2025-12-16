package com.github.aeoliux.app.content.messages

import androidx.compose.material3.MaterialShapes
import androidx.compose.ui.graphics.Color
import androidx.graphics.shapes.RoundedPolygon
import androidx.lifecycle.viewModelScope
import com.github.aeoliux.api.scraping.messages.MessageCategories
import com.github.aeoliux.app.RefreshableViewModel
import com.github.aeoliux.app.content.minimalFormat
import com.github.aeoliux.app.content.onlyHourAndMinute
import com.github.aeoliux.repositories.MessagesRepository
import com.github.aeoliux.storage.MessageLabel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.datetime.LocalTime

class MessagesViewModel(
    private val messagesRepository: MessagesRepository
): RefreshableViewModel() {
    private var _category = MutableStateFlow(MessageCategories.Received)
    val category get() = _category.asStateFlow()

    private var _query = MutableStateFlow("")
    val query get() = _query.asStateFlow()

    val categoriesOrdered = this._category
        .map { category ->
            listOf(category)
                .plus(
                    MessageCategories.entries
                        .filter { it != category }
                )
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _core = this._query
        .flatMapLatest { query ->
            this.messagesRepository
                .getLabelsFlow(query.takeIf { it.isNotEmpty() })
        }

    val messageLabels = combine(
        this._core,
        this._category,
        this._query
    ) { labels, category, searchQuery ->
        labels[category]?.map {
            MessageMetadata(
                senderLabel = {
                    val split = it.sender.split(" ")
                    ((split.getOrNull(0)?.uppercase()?.substring(0, 1) ?: "")
                            +
                            (split.getOrNull(1)?.uppercase()?.substring(0, 1) ?: ""))
                }(),
                messageLabel = it,
                parsedDatetime = it.sentAt?.date?.minimalFormat()?.let { date ->
                    it.sentAt?.time?.onlyHourAndMinute()?.let { time ->
                        Pair(date, time)
                    }
                },
                theme = it.sentAt?.time
                    ?.getThemeForOur()
                    ?: Pair(Color.Black, MaterialShapes.Ghostish)
            )
        } ?: emptyList()
    }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun setCategory(category: MessageCategories) = this._category.update { category }
    fun setQuery(query: String) = this._query.update { query }

    fun refresh() = this.task {
        this.messagesRepository.refresh()
    }

    data class MessageMetadata(
        val theme: Pair<Color, RoundedPolygon>,
        val senderLabel: String,
        val messageLabel: MessageLabel,
        val parsedDatetime: Pair<String, String>?
    )
}

fun LocalTime.getThemeForOur(): Pair<Color, RoundedPolygon> = this.hour.let {
    when {
        it > 22 || it < 2 -> Pair(Color.DarkGray, MaterialShapes.Slanted)
        it > 20 || it < 4 -> Pair(Color.LightGray, MaterialShapes.Arrow)
        it > 18 || it < 6 -> Pair(Color.Red, MaterialShapes.Cookie4Sided)
        it > 16 || it < 8 -> Pair(Color.Green, MaterialShapes.Flower)
        it > 14 || it < 10 -> Pair(Color.Blue, MaterialShapes.Sunny)
        else -> Pair(Color.Cyan, MaterialShapes.SoftBurst)
    }
}