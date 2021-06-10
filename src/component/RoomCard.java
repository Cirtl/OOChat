package src.component;

import src.entity.House;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RoomCard extends JPanel {
    private House room;
    private final JButton button;

    public RoomCard(House room) {
        super();
        this.room = room;
        SpringLayout layout = new SpringLayout();
        this.setLayout(layout);
        JLabel label = new JLabel();
        label.setText(room.getName());
        label.setFont(new Font(null, Font.BOLD, 25));
        button = new JButton("申请加入");
        button.setFont(new Font(null, Font.PLAIN, 18));

        this.add(label);
        this.add(button);

        SpringLayout.Constraints labelCons = layout.getConstraints(label);
        labelCons.setX(Spring.constant(5));
        labelCons.setY(Spring.constant(5));

        SpringLayout.Constraints btnCons = layout.getConstraints(button);
        btnCons.setX(Spring.constant(135));
        btnCons.setY(Spring.constant(5));

        SpringLayout.Constraints panelCons = layout.getConstraints(this);
        panelCons.setConstraint(
                SpringLayout.EAST,
                Spring.sum(
                        btnCons.getConstraint(SpringLayout.EAST),
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

    public House getRoom() {
        return room;
    }

    public JButton getButton() {
        return button;
    }
}
