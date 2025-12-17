import Shared
import SwiftUI

extension MessagesView {
    @Observable
    class ViewModel: RefreshableViewModel {
        var categories = [(MessageCategories, Bool)]()
        var labels = [MessageCategories: [(String, SwiftUI.Color, Date?, MessageLabel_)]]()
        var labelsTask: Task<(), Never>?
        
        let repos = RepositoryHelper()
        
        override init() {
            super.init()
            
            self.labelsTask = Task {
                for await labels in self.repos.messagesRepository.getLabelsFlow() {
                    self.labels = labels.reduce([MessageCategories: [(String, SwiftUI.Color, Date?, MessageLabel_)]]()) { (acc, item) in
                        var acc = acc
                        
                        guard
                            let category = item.key as? MessageCategories,
                            let labels = item.value as? [MessageLabel_]
                        else { return acc }
                        
                        acc.updateValue(
                            (labels.map { label in
                                (label.sender.firstLettersLabel(), label.sentAt.themeForTime(), label.sentAt?.toNSDate(), label)
                            }),
                            forKey: category
                        )
                        
                        return acc
                    }
                    
                    self.categories = labels.compactMap { (category, _) in
                        guard let category = category as? MessageCategories else { return nil }
                        
                        return (category, category == MessageCategories.received)
                    }
                }
            }
        }
        
        deinit {
            self.labelsTask?.cancel()
        }
        
        func refresh() async{
            await self.task {
                try await self.repos.messagesRepository.refresh()
            }
        }
    }
}

extension Kotlinx_datetimeLocalDateTime? {
    func themeForTime() -> SwiftUI.Color {
        return switch (Int(self?.toNSDate().timeIntervalSince1970 ?? 2) % 10) {
        case 0...1:
                .cyan
        case 2...3:
                .red
        case 4...5:
                .green
        case 6...7:
                .orange
        case 7...8:
                .purple
        default:
                .mint
        }
    }
}

extension String {
    func firstLettersLabel() -> String {
        let uppercased = self.uppercased()
        let split = uppercased.split(separator: " ")
        
        let letters = split.map { item in
            item.count > 0 ? String(item.first ?? Character(" ")) : ""
        }[0...1]
        
        return letters.joined()
    }
}
