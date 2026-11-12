package br.com.vitalis;

import br.com.vitalis.ui.TelaLogin;
import javax.swing.*;

public class VitalisApp {
    public static void main(String[] args) {
    try {
        System.setOut(new java.io.PrintStream(System.out, true, "UTF-8"));
        System.setErr(new java.io.PrintStream(System.err, true, "UTF-8"));
    } catch (Exception ignored) {}

    SwingUtilities.invokeLater(() -> new TelaLogin().setVisible(true));
    }
}