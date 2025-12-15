import Shared
import SwiftUI

struct GradesView: View {
    @State var viewModel = ViewModel()
    
    var body: some View {
        NavigationStack {
            List {
                if !self.viewModel.averages.isEmpty {
                    Section {
                        ForEach(self.viewModel.averages.indices, id: \.self) { index in
                            HStack {
                                Spacer()
                                
                                ForEach(self.viewModel.averages[index], id: \.0) { (label, color, average) in
                                    VStack {
                                        Circle()
                                            .fill(color)
                                            .frame(width: 50, height: 50)
                                            .overlay {
                                                Text(average)
                                                    .foregroundStyle(.white)
                                            }
                                        
                                        Text(label)
                                            .frame(width: 175)
                                    }
                                    
                                    Spacer()
                                }
                            }
                        }
                    }
                }
                
                Section("Subjects") {
                    ForEach(self.viewModel.subjects, id: \.self) { subject in
                        NavigationLink(value: subject) {
                            Text(subject)
                        }
                    }
                }
            }
            .refreshable { await self.viewModel.refresh() }
            .navigationTitle("Grades")
            .navigationDestination(for: String.self) { subject in
                GradesPerSubjectView(subject)
                    .navigationTitle(subject)
            }
        }
    }
}
