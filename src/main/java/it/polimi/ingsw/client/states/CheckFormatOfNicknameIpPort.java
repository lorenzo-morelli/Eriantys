/*
package it.polimi.ingsw.client.states;

import it.polimi.ingsw.client.Model;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.utils.cli.CommandPrompt;
import it.polimi.ingsw.utils.stateMachine.Event;
import it.polimi.ingsw.utils.stateMachine.IEvent;
import it.polimi.ingsw.utils.stateMachine.State;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

 * stato per controllare che i parametri inseriti nello stato CreateOrConnec siano della forma:
 *
 * nickname non nullo (es. Pippo)
 * ip fatto da x.y.z.q (es. 192.168.1.1)
 * porta fatta da un numero (es. 1234)


public class CheckFormatOfNicknameIpPort extends State {
    private Model model;
    private View view;
    private Event formatoCorretto;
    private Event formatoNonCorretto;

    public CheckFormatOfNicknameIpPort(View view, Model model) {
        super("[Controllo di correttezza nick ip porta]]");
        this.model = model;
        this.view = view;
        formatoCorretto = new Event("Formato dei dati corretto");
        formatoNonCorretto = new Event("Formato dei dati non corretto");
    }

    public Event formatoCorretto() {
        return formatoCorretto;
    }

    public Event formatoNonCorretto() {
        return formatoNonCorretto;
    }

    @Override
    public IEvent entryAction(IEvent cause) throws IOException, InterruptedException {
        view.setCallingState(this);

        // Quì andranno implementati tutti i controlli di correttezza
        ArrayList ipPieces = new ArrayList<String>(Arrays.asList(model.getIp().split(".")));
        if (    ipPieces.size() == 4  &&
                Pattern.matches("[0-9]*", model.getPort()) ){
                // L'ip è composto da 4 pezzi e la porta è un numero
                // Ora devo verificare che l'ip sia composto da numeri
            System.out.println("Primo controllo ok");


            if(     Pattern.matches("[0-9]*", ipPieces.get(0).toString()) &&
                    Pattern.matches("[0-9]*", ipPieces.get(1).toString())  &&
                    Pattern.matches("[0-9]*", ipPieces.get(2).toString()) &&
                    Pattern.matches("[0-9]*", ipPieces.get(3).toString())
                ){
                    formatoCorretto.fireStateEvent();
            }
            else {
                formatoNonCorretto.fireStateEvent();
            }
        }
        else {
            formatoNonCorretto.fireStateEvent();
        }

        return super.entryAction(cause);

    }
}
*/