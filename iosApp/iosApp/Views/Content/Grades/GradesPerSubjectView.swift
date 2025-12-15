import Shared
import SwiftUI

struct GradesPerSubjectView: View {
    @State var viewModel: ViewModel
    
    init(_ subject: String) {
        self.viewModel = ViewModel(subject)
    }
    
    var body: some View {
        List {
            ForEach(self.viewModel.grades.indices, id: \.self) { semester in
                if !self.viewModel.grades[semester].isEmpty {
                    Section("Semester \(semester + 1)") {
                        ForEach(self.viewModel.grades[semester], id: \.grade.id) { grade in
                            NavigationLink(value: grade.grade) {
                                HStack {
                                    VStack(alignment: .leading) {
                                        Text(grade.grade.addedBy)
                                            .font(.callout)
                                        Text(grade.grade.category)
                                            .font(.callout)
                                    }
                                    
                                    Spacer()
                                    
                                    Rectangle()
                                        .fill(grade.color)
                                        .frame(width: 40, height: 40)
                                        .clipShape(RoundedRectangle(cornerRadius: 5))
                                        .overlay {
                                            Text("\(grade.grade.grade)")
                                                .foregroundStyle(Color.black)
                                        }
                                }
                            }
                        }
                    }
                }
            }
            .refreshable { await self.viewModel.refresh() }
        }
        .navigationDestination(for: Grade_.self) { grade in
            GradeView(grade)
        }
    }
}
