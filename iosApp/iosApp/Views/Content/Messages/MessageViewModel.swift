import Shared
import SwiftUI

extension MessageView {
    @Observable
    class ViewModel {
        var messageTask: Task<(), Never>?
        var tabs: [(String, String, AttributedString?)] = []
        
        let repos = RepositoryHelper()
        
        init(_ messageLabel: MessageLabel_) {
            self.messageTask = Task {
                for await message in self.repos.messagesRepository.getMessageFlow(url: messageLabel.url) {
                    if let message = message {
                        self.tabs = [
                            ("person.fill", "By", AttributedString(html: messageLabel.sender)),
                            ("exclamationmark.bubble.fill", "Topic", AttributedString(html: messageLabel.topic)),
                            ("calendar.badge.clock", "Sent at",
                                messageLabel.sentAt != nil ? AttributedString(html: datetimeFormat.string(from: messageLabel.sentAt!.toNSDate())) : nil),
                            ("envelope.fill", "Content", AttributedString(html: message.content))
                        ]
                    }
                }
            }
        }
        
        deinit {
            self.messageTask?.cancel()
        }
    }
}

extension AttributedString {
    init?(html: String) {
        guard
            let nsAttribStr = try? NSAttributedString(
                data: Data(html.utf8),
                options: [
                    .documentType: NSAttributedString.DocumentType.html,
                    .characterEncoding: String.Encoding.utf8.rawValue
                ],
                documentAttributes: nil
            ),
            var attribStr = try? AttributedString(nsAttribStr, including: \.uiKit)
        else { return nil }
        
        attribStr.font = .system(.body)
        attribStr.foregroundColor = .primary
        
        self = attribStr
    }
}
