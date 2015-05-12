/*package layers.application;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by HP on 11.05.2015.

public class InfoForm {

    private JPanel panel1;
    private JButton okay;
    private JTextArea infaText;
    private ApplicationLayer applicationLayer;
    private FileForm linkToFileForm;
    private InfoForm linkToHimself;

    public InfoForm(JFrame linkToFrame) {
        okay.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FileForm FF = new FileForm(linkToFrame);
                FF.wannaFile(getApplicationLayer());
                applicationLayer.giveLinkToHimself().getLinkToAppl().getInfoForm().setLinkToFileForm(FF);

                linkToFrame.setVisible(false);

            }
        });
    }


    public void wannaFileInfo(ApplicationLayer applicationLayer) {
        setApplicationLayer(applicationLayer);
        JFrame frame = new JFrame("InfoForm");

        frame.setContentPane(new InfoForm(frame).panel1);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public FileForm getLinkToFileForm() {
        return linkToFileForm;
    }

    public void setLinkToFileForm(FileForm linkToFileForm) {
        this.linkToFileForm = linkToFileForm;
    }

    public InfoForm getLinkToHimself() {
        return linkToHimself;
    }

    public void setLinkToHimself(InfoForm linkToHimself) {
        this.linkToHimself = linkToHimself;
    }

    public ApplicationLayer getApplicationLayer() {
        return applicationLayer;
    }

    public void setApplicationLayer(ApplicationLayer applicationLayer) {
        this.applicationLayer = applicationLayer;
    }
}
*/