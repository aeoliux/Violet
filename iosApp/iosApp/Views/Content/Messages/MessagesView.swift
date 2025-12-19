import Shared
import SwiftUI

struct MessagesView: View {
    @State var viewModel = ViewModel()
    
    var body: some View {
        List {
            Section("Show categories") {
                ForEach(self.viewModel.categories.indices, id: \.self) { index in
                    let category = self.viewModel.categories[index].0
                    
                    Toggle(category.name, isOn: self.$viewModel.categories[index].1)
                }
            }
            
            ForEach(self.viewModel.categories.filter { $0.1 }, id: \.0.categoryId) { category in
                Section(category.0.name) {
                    if let labels = self.viewModel.labels[category.0] {
                        ForEach(labels, id: \.3.key) { (firstLetters, color, date, label) in
                            NavigationLink(value: label) {
                                Circle()
                                    .fill(color)
                                    .frame(width: 55, height: 55)
                                    .overlay {
                                        Text(firstLetters)
                                            .foregroundStyle(Color.white)
                                    }
                                
                                VStack(alignment: .leading) {
                                    Text(label.topic)
                                        .lineLimit(1)
                                        .frame(alignment: .leading)
                                        .fontWeight(.semibold)
                                    
                                    Text(label.sender)
                                        .lineLimit(1)
                                        .frame(alignment: .leading)
                                    
                                    if let date = date {
                                        Text(datetimeFormat.string(from: date))
                                            .lineLimit(1)
                                            .font(.footnote)
                                            .frame(alignment: .leading)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        .refreshable { await self.viewModel.refresh() }
        .toolbar {
            ToolbarItem(placement: .topBarTrailing) {
                NavigationLink(value: MessageEditorRoute(label: nil, message: nil)) {
                    Image(systemName: "paperplane")
                }
            }
        }
        .navigationTitle("Messages")
    }
}

//extension [(Date?, MessageLabel_)]
