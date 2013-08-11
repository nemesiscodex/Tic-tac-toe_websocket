/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ttt;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WebSocketServlet;
import org.apache.catalina.websocket.WsOutbound;
import com.google.gson.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Julio
 */
public class Server extends WebSocketServlet {

    private static final long serialVersionUID = 1L;
    private static ArrayList<MiMensaje> mmiList = new ArrayList<MiMensaje>();

    @Override
    protected StreamInbound createWebSocketInbound(String string, HttpServletRequest hsr) {
        return new MiMensaje();
    }

    class MiMensaje extends MessageInbound {

        WsOutbound myoutbound;

        @Override
        public void onOpen(WsOutbound outbound) {
            System.out.println("Open Client.");
            Gson gson = new Gson();
            this.myoutbound = outbound;
            GameObject go = new GameObject();
            int size = mmiList.size();
            try {
                go.tipo = 0;
                go.jugada = -1;
                go.nombre = "Jugador " + (size + 1);
                if (size == 0) {
                    go.jugador = true;
                    mmiList.add(this);
                } else if (size == 1) {
                    go.jugador = false;
                    go.tipo = 5;
                    mmiList.get(0).myoutbound.writeTextMessage(CharBuffer.wrap(gson.toJson(go)));
                    go.tipo = 0;
                    mmiList.add(this);
                } else {
                    go.tipo = -1;
                }

                outbound.writeTextMessage(CharBuffer.wrap(gson.toJson(go)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onClose(int status) {
            System.out.println("Close Client.");
            if (!mmiList.contains(this)) {
                return;
            }
            mmiList.remove(this);
            Gson gson = new Gson();
            GameObject go = new GameObject();
            go.tipo = 3;
            go.jugador = true;
            go.nombre = "";
            go.jugada = -1;
            try {
                for (MiMensaje m : mmiList) {
                    m.myoutbound.writeTextMessage(CharBuffer.wrap(gson.toJson(go)));
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void onTextMessage(CharBuffer cb) throws IOException {
            System.out.println("Accept Message : " + cb);
            for (MiMensaje mmib : mmiList) {
                CharBuffer buffer = CharBuffer.wrap(cb);
                mmib.myoutbound.writeTextMessage(buffer);
                mmib.myoutbound.flush();
            }
        }

        @Override
        public void onBinaryMessage(ByteBuffer bb) throws IOException {
        }
    }
}
