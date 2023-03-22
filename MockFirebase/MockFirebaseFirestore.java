package edu.ucsd.cse110.team19.walkwalkrevolution.MockFirebase;

import java.util.Map;

public class MockFirebaseFirestore {

    private MockCollection mockCollection;


    public MockFirebaseFirestore() {
        mockCollection = new MockCollection();
    }

    public MockFirebaseFirestore getInstance() {
        return this;
    }

    public MockCollection collection(String collectionName) {
        mockCollection.mockCollectionName = collectionName;
        return mockCollection;
    }

    public static class MockCollection {
        String mockCollectionName;
        private MockDocument mockDocument;

        public MockCollection() {
            mockDocument = new MockDocument();
        }

        public MockDocument document(String documentName) {
            mockDocument.mockDocumentName = documentName;
            return mockDocument;
        }

        public static class MockDocument {
            String mockDocumentName;
            Map<String,Object> documentMap;

            public void set(Map<String,Object> map) {
                documentMap = map;
            }

            public Map<String,Object> get() {
                return documentMap;
            }

        }
    }
}
