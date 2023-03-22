package edu.ucsd.cse110.team19.walkwalkrevolution.MockFirebase;

public class MockFirebaseAuth {

    private MockFirebaseUser mockFirebaseUser;

    public MockFirebaseAuth() {
        mockFirebaseUser = new MockFirebaseUser();
    }

    public MockFirebaseAuth getInstance() {
        return this;
    }

    public MockFirebaseUser getCurrentUser() {
        return mockFirebaseUser;
    }


    public static class MockFirebaseUser {
        String name = "MOCK_NAME";
        String UID = "MOCK_UID";
        String email = "MOCK_EMAIL";
        String token = "MOCK_TOKEN";

        public String getUid() {
            return UID;
        }

        public String getDisplayName() {
            return name;
        }

        public String getEmail() {
            return email;
        }

        public String getToken() {
            return token;
        }

    }

}
