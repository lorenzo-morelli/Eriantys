package it.polimi.ingsw.client.view;

public interface View {

    String askCiao();

    String askNickname();

    String askNicknameConfirmation(String nickname);

    void showConfirmation(String nickname);

}
