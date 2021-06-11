package src.component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class MemberCard extends JPanel {

    public MemberCard(String userId, ActionListener whisperListener, ActionListener kickListener) {
        super();
        
        SpringLayout layout = new SpringLayout();
        this.setLayout(layout);
        JLabel label = new JLabel();
        label.setText(userId);
        label.setFont(new Font(null, Font.BOLD, 20));
        JButton whisperButton = new JButton("私聊");
        whisperButton.setFont(new Font(null, Font.PLAIN, 16));
        whisperButton.addActionListener(whisperListener);
        JButton kickButton = new JButton("踢出");
        kickButton.setFont(new Font(null, Font.PLAIN, 16));
        kickButton.addActionListener(kickListener);

        this.add(label);
        this.add(whisperButton);
        this.add(kickButton);

        SpringLayout.Constraints labelCons = layout.getConstraints(label);
        labelCons.setX(Spring.constant(5));
        labelCons.setY(Spring.constant(5));

        SpringLayout.Constraints applyCons = layout.getConstraints(whisperButton);
        applyCons.setX(Spring.sum(
                labelCons.getConstraint(SpringLayout.EAST),
                Spring.constant(15)
        ));
        applyCons.setY(Spring.constant(5));

        SpringLayout.Constraints deleteCons = layout.getConstraints(kickButton);
        deleteCons.setX(Spring.sum(
                applyCons.getConstraint(SpringLayout.EAST),
                Spring.constant(15)
        ));
        deleteCons.setY(Spring.constant(5));

        SpringLayout.Constraints panelCons = layout.getConstraints(this);
        panelCons.setConstraint(
                SpringLayout.EAST,
                Spring.sum(
                        deleteCons.getConstraint(SpringLayout.EAST),
                        Spring.constant(5)
                )
        );
        panelCons.setConstraint(
                SpringLayout.SOUTH,
                Spring.sum(
                        labelCons.getConstraint(SpringLayout.SOUTH),
                        Spring.constant(5)
                )
        );

        this.setVisible(true);
        this.validate();
    }
}
