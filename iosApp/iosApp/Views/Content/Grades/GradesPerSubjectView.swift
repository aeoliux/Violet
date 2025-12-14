import Shared
import SwiftUI

struct GradesPerSubjectView: View {
    let grades: [GradesView.NavKey.GradeInfo]
    let refresh: () async -> Void
    
    var body: some View {
        List {
            ForEach(grades, id: \.grade.id) { grade in
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
        .refreshable { await self.refresh() }
    }
}
