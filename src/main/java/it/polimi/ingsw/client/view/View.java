package it.polimi.ingsw.client.view;

public interface View {

    void askToStart();

    void askNickname();

    void askNicknameConfirmation(String nickname);

    void showConfirmation(String nickname);

}
