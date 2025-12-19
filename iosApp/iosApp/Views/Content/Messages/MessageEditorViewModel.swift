import Shared
import SwiftUI

extension MessageEditorView {
    @Observable
    class ViewModel {
        let repos = RepositoryHelper()
        
        var allUsers = [User]()
        var selectedUsers = [User]()
        var topic = ""
        var content = ""
        
        var requestKey = ""
        var respondsTo: String?
        
        var usersSelector = false
        
        var initialTask: Task<(), Error>?
        
        init(message: Message_?, messageLabel: MessageLabel_?) {
            self.initialTask = Task {
                self.allUsers = try await self.repos.messagesRepository.requestUsers()
                    .compactMap { (id, user) in
                        user as? User
                    }
                    .filter { user in user.group == 4 || user.group == 1 }
                    .sorted { (user1, user2) in "\(user1.lastName) \(user1.lastName)".lowercased() < "\(user2.lastName) \(user1.firstName)".lowercased() }
                
                if let message = message, let messageLabel = messageLabel {
                    self.respondsTo = String(messageLabel.key)
                    
                    self.topic = "Re: \(messageLabel.topic)"
                    self.content = """


-----
Użytkownik \(messageLabel.sender) \(messageLabel.sentAt != nil ? messageLabel.sentAt!.toNSDate().formatted() : "") napisał:
\(message.content.replacingOccurrences(of: "<br>", with: ""))
"""
                    
                    let senderSplit = messageLabel.sender.split(separator: " ")
                    if senderSplit.count >= 2, let receiver = (self.allUsers.first { $0.firstName == senderSplit[1] && $0.lastName == senderSplit[0] }) {
                        self.selectedUsers = [receiver]
                    }
                }
                
                self.requestKey = try await self.repos.messagesRepository.initializeSender(respondsTo: self.respondsTo)
            }
        }
        
        func sendMessage() async throws {
            try await self.repos.messagesRepository.sendMessage(
                topic: self.topic,
                content: self.content,
                users: self.selectedUsers,
                key: self.requestKey,
                respondsTo: self.respondsTo
            )
        }
        
        deinit {
            self.initialTask?.cancel()
        }
    }
}
