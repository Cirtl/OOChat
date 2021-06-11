package src.component;

import src.entity.Room;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class RoomCard extends JPanel {

    public RoomCard(Room room, ActionListener applyListener, ActionListener deleteListener) {
        super();
        SpringLayout layout = new SpringLayout();
        this.setLayout(layout);
        JLabel label = new JLabel();
        label.setText(String.valueOf(room.getId()));
        label.setFont(new Font(null, Font.BOLD, 25));
        JButton applyButton = new JButton("申请加入");
        applyButton.setFont(new Font(null, Font.PLAIN, 18));
        applyButton.addActionListener(applyListener);
        JButton deleteButton = new JButton("删除房间");
        deleteButton.setFont(new Font(null, Font.PLAIN, 18));
        deleteButton.addActionListener(deleteListener);

        this.add(label);
        this.add(applyButton);
        this.add(deleteButton);

        SpringLayout.Constraints labelCons = layout.getConstraints(label);
        labelCons.setX(Spring.constant(5));
        labelCons.setY(Spring.constant(5));

        SpringLayout.Constraints applyCons = layout.getConstraints(applyButton);
        applyCons.setX(Spring.sum(
                labelCons.getConstraint(SpringLayout.EAST),
                Spring.constant(15)
        ));
        applyCons.setY(Spring.constant(5));

        SpringLayout.Constraints deleteCons = layout.getConstraints(deleteButton);
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
