package modals

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "messages_table")
data class Message(
    @PrimaryKey @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "message") val message: String,
    @ColumnInfo(name = "is_received_message") val isReceivedMessage: Boolean,
    @ColumnInfo(name = "parent_id") val parentId: Int,
    @ColumnInfo(name = "message_order") val order: Int,
)
