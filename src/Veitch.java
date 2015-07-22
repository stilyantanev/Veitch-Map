import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Veitch extends JFrame {

    private static final long serialVersionUID = 1L;

    static int workArray[][] = new int[4][4];
    static int checkedIndexes[][] = new int[4][4];
    static int valuesTable[] = new int[16];
    static String result = "";

    JPanel table;
    JLabel tableDescription;
    JLabel textResult;
    JLabel horizontalLabels[] = new JLabel[4];
    JLabel verticalLabels[] = new JLabel[4];
    JButton buttons[] = new JButton[16];
    JButton minimize;
    JButton reset;

    public Veitch() {
        // Add new frame
        JFrame frame = new JFrame();
        frame.setTitle("Минимизация на булеви функции чрез карти на Вейч");
        frame.setSize(950, 650);
        frame.setLocation(100, 50);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLayout(null);

        // Add new text for result of minimization
        textResult = new JLabel("f = ");
        textResult.setForeground(Color.BLACK);
        textResult.setLayout(null);
        textResult.setFont(new Font("Arial", Font.BOLD, 20));
        textResult.setBounds(10, 560, 1000, 50);
        frame.add(textResult);

        // Add new text for description of table
        tableDescription = new JLabel("КАРТА НА ВЕЙЧ с 4 на брой променливи: x1, x2, x3, x4");
        tableDescription.setForeground(Color.BLACK);
        tableDescription.setLayout(null);
        tableDescription.setFont(new Font("Arial", Font.BOLD, 20));
        tableDescription.setBounds(250, 0, 600, 100);
        frame.add(tableDescription);

        // Put two vertical labels on the left side
        String leftSideLabel = "x2";
        int yLeftSideDisplacement = 120;

        for (int i = 0; i < 2; i++) {
            verticalLabels[i] = new JLabel(leftSideLabel);
            verticalLabels[i].setLayout(null);
            verticalLabels[i].setFont(new Font("Arial", Font.BOLD, 20));
            verticalLabels[i].setBounds(290, yLeftSideDisplacement, 100, 100);
            frame.add(verticalLabels[i]);
            yLeftSideDisplacement += 100;
        }

        // Put two vertical labels on the right side
        String rightSideLabel = "x4";
        int yRightSideDisplacement = 220;

        for (int i = 2; i < 4; i++) {
            verticalLabels[i] = new JLabel(rightSideLabel);
            verticalLabels[i].setLayout(null);
            verticalLabels[i].setFont(new Font("Arial", Font.BOLD, 20));
            verticalLabels[i].setBounds(740, yRightSideDisplacement, 100, 100);
            frame.add(verticalLabels[i]);
            yRightSideDisplacement += 100;
        }

        // Put two horizontal labels on the up side
        String upSideLabel = "x1";
        int xUpSideDisplacement = 370;

        for (int i = 0; i < 2; i++) {
            horizontalLabels[i] = new JLabel(upSideLabel);
            horizontalLabels[i].setLayout(null);
            horizontalLabels[i].setFont(new Font("Arial", Font.BOLD, 20));
            horizontalLabels[i].setBounds(xUpSideDisplacement, 50, 100, 100);
            frame.add(horizontalLabels[i]);
            xUpSideDisplacement += 100;
        }

        // Put two horizontal labels on the down side
        String downSideLabel = "x3";
        int xDownSideDispalcement = 470;

        for (int i = 2; i < 4; i++) {
            horizontalLabels[i] = new JLabel(downSideLabel);
            horizontalLabels[i].setLayout(null);
            horizontalLabels[i].setFont(new Font("Arial", Font.BOLD, 20));
            horizontalLabels[i].setBounds(xDownSideDispalcement, 490, 100, 100);
            frame.add(horizontalLabels[i]);
            xDownSideDispalcement += 100;
        }

        // Add new table
        table = new JPanel();
        table.setBounds(330, 120, 400, 400);
        table.setBackground(Color.BLUE);
        table.setLayout(new GridLayout(4, 4));
        frame.add(table);

        // Add buttons for table
        for (int i = 0; i < 16; i++) {
            buttons[i] = new JButton();
            buttons[i].setIcon(new ImageIcon(getClass().getResource("resources/0.png")));
            table.add(buttons[i]);
            table.validate();
        }

        // Add button for minimization
        minimize = new JButton("Минимизирай");
        minimize.setForeground(Color.BLACK);
        minimize.setBounds(60, 260, 150, 40);
        frame.add(minimize);

        // Add button for reset
        reset = new JButton("Изчисти");
        reset.setForeground(Color.BLACK);
        reset.setBounds(60, 320, 150, 40);
        frame.add(reset);

        // Set frame visible
        frame.setVisible(true);

        // Change button value 0 or 1
        for (int i = 0; i < 16; i++) {

            int current_index = i;
            buttons[i].addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    valuesTable[current_index]++;
                    valuesTable[current_index] %= 2;

                    if (valuesTable[current_index] == 0) {
                        buttons[current_index].setIcon(new ImageIcon(getClass().getResource("resources/0.png")));
                    }

                    if (valuesTable[current_index] == 1) {
                        buttons[current_index].setIcon(new ImageIcon(getClass().getResource("resources/1.png")));
                    }
                }
            });
        }

        // Reset button action
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textResult.setText("f = ");
                result = "";

                for (int i = 0; i < 16; i++) {
                    buttons[i].setIcon(new ImageIcon(getClass().getResource("resources/0.png")));
                    valuesTable[i] = 0;
                }
            }
        });

        // Minimize button action
        minimize.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                result = "";
                compute();

                // Delete " + " combination
                if (result.charAt(0) == ' ') {
                    result = result.substring(3, result.length());
                }

                textResult.setText("f = " + result);
            }
        });
    }

    // Main algorithm
    static void compute() {

        // Initialize workArray with User Input
        initialize();

        // Check table for 16 one's
        boolean have16 = check16();
        if (have16) {
            return;
        }

        // Check table for 8 one's, 4 one's, 2 one's, 1 one's
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (workArray[i][j] == 1 && checkedIndexes[i][j] == 0) {
                    boolean have8 = check8(i, j);
                    if (!have8) {
                        boolean have4 = check4(i, j);
                        if (!have4) {
                            boolean have2 = check2(i, j);
                            if (!have2) {
                                check1(i, j);
                            }
                        }
                    }
                }
            }
        }

        // Check table for 0 one's
        boolean have0 = check0();
        if (have0) {
            return;
        }
    }

    static boolean check16() {
        boolean isElements16 = false;
        int counterForOne = 0;
        String localResult = "1";

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (workArray[i][j] == 1) {
                    counterForOne++;
                }
            }
        }

        if (counterForOne == 16) {
            result = localResult;
            isElements16 = true;
        }

        return isElements16;
    }

    static boolean check8(int row, int col) {
        boolean isElements8 = false;
        String localResult = "";

        // Case 1 Top to Bottom Horizontal
        if (workArray[row][0] == 1 && workArray[row][1] == 1 &&
            workArray[row][2] == 1 && workArray[row][3] == 1 &&
            workArray[(row + 1) % 4][0] == 1 && workArray[(row + 1) % 4][1] == 1 &&
            workArray[(row + 1) % 4][2] == 1 && workArray[(row + 1) % 4][3] == 1) {

            String[] yLocalValues = { "x2", "x4", "x2'", "x4'" };

            for (int i = 0; i < 4; i++) {
                if (row == i) {
                    localResult = yLocalValues[i];
                }
            }

            isElements8 = true;

            // Add to result current group
            result = result + " + " + localResult;

            // Check current indexes
            checkedIndexes[row][0] = 1;
            checkedIndexes[row][1] = 1;
            checkedIndexes[row][2] = 1;
            checkedIndexes[row][3] = 1;
            checkedIndexes[(row + 1) % 4][0] = 1;
            checkedIndexes[(row + 1) % 4][1] = 1;
            checkedIndexes[(row + 1) % 4][2] = 1;
            checkedIndexes[(row + 1) % 4][3] = 1;
        }

        // Case 2 Bottom to Top Horizontal
        else if (workArray[row][0] == 1 && workArray[row][1] == 1 &&
                 workArray[row][2] == 1 && workArray[row][3] == 1 &&
                 workArray[(4 + (row - 1)) % 4][0] == 1 && workArray[(4 + (row - 1)) % 4][1] == 1 &&
                 workArray[(4 + (row - 1)) % 4][2] == 1 && workArray[(4 + (row - 1)) % 4][3] == 1) {

            String[] ySideLocal = { "x4'", "x2", "x4", "x2'" };

            for (int i = 0; i < 4; i++) {
                if (row == i) {
                    localResult = ySideLocal[i];
                }
            }

            isElements8 = true;

            // Add to result current group
            result = result + " + " + localResult;

            // Check current indexes
            checkedIndexes[row][0] = 1;
            checkedIndexes[row][1] = 1;
            checkedIndexes[row][2] = 1;
            checkedIndexes[row][3] = 1;
            checkedIndexes[(4 + (row - 1)) % 4][0] = 1;
            checkedIndexes[(4 + (row - 1)) % 4][1] = 1;
            checkedIndexes[(4 + (row - 1)) % 4][2] = 1;
            checkedIndexes[(4 + (row - 1)) % 4][3] = 1;
        }

        // Case 3 Left To Right Vertical
        else if (workArray[0][col] == 1 && workArray[1][col] == 1 &&
                 workArray[2][col] == 1 && workArray[3][col] == 1 &&
                 workArray[0][(col + 1) % 4] == 1 && workArray[1][(col + 1) % 4] == 1 &&
                 workArray[2][(col + 1) % 4] == 1 && workArray[3][(col + 1) % 4] == 1) {

            String[] xSideLocal = { "x1", "x3", "x1'", "x3'" };

            for (int i = 0; i < 4; i++) {
                if (col == i) {
                    localResult = xSideLocal[i];
                }
            }

            isElements8 = true;

            // Add to result current group
            result = result + " + " + localResult;

            // Check current indexes
            checkedIndexes[0][col] = 1;
            checkedIndexes[1][col] = 1;
            checkedIndexes[2][col] = 1;
            checkedIndexes[3][col] = 1;
            checkedIndexes[0][(col + 1) % 4] = 1;
            checkedIndexes[1][(col + 1) % 4] = 1;
            checkedIndexes[2][(col + 1) % 4] = 1;
            checkedIndexes[3][(col + 1) % 4] = 1;
        }

        // Case 4 Right to Left Vertical
        else if (workArray[0][col] == 1 && workArray[1][col] == 1 &&
                 workArray[2][col] == 1 && workArray[3][col] == 1 &&
                 workArray[0][(4 + (col - 1)) % 4] == 1 && workArray[1][(4 + (col - 1)) % 4] == 1 &&
                 workArray[2][(4 + (col - 1)) % 4] == 1 && workArray[3][(4 + (col - 1)) % 4] == 1) {

            String[] xSideLocal = { "x3'", "x1", "x3", "x1'" };

            for (int i = 0; i < 4; i++) {
                if (col == i) {
                    localResult = xSideLocal[i];
                }
            }

            isElements8 = true;

            // Add to result current group
            result = result + " + " + localResult;

            // Check current indexes
            checkedIndexes[0][col] = 1;
            checkedIndexes[1][col] = 1;
            checkedIndexes[2][col] = 1;
            checkedIndexes[3][col] = 1;
            checkedIndexes[0][(4 + (col - 1)) % 4] = 1;
            checkedIndexes[1][(4 + (col - 1)) % 4] = 1;
            checkedIndexes[2][(4 + (col - 1)) % 4] = 1;
            checkedIndexes[3][(4 + (col - 1)) % 4] = 1;
        }

        return isElements8;
    }

    static boolean check4(int row, int col) {
        boolean isElements4 = false;
        String localResult = "";

        // Case 1 Horizontal
        if (workArray[row][0] == 1 && workArray[row][1] == 1 &&
            workArray[row][2] == 1 && workArray[row][3] == 1) {

            String[] ySideLocal = { "x2x4'", "x2x4", "x2'x4", "x2'x4'" };

            for (int i = 0; i < 4; i++) {
                if (row == i) {
                    localResult = localResult + ySideLocal[i];
                }
            }

            isElements4 = true;

            // Add to result current group
            result = result + " + " + localResult;

            // Check current indexes
            checkedIndexes[row][0] = 1;
            checkedIndexes[row][1] = 1;
            checkedIndexes[row][2] = 1;
            checkedIndexes[row][3] = 1;
        }

        // Case 2 Vertical
        else if (workArray[0][col] == 1 && workArray[1][col] == 1 &&
                 workArray[2][col] == 1 && workArray[3][col] == 1) {

            String[] xSideLocal = { "x1x3'", "x1x3", "x1'x3", "x1'x3'" };

            for (int i = 0; i < 4; i++) {
                if (col == i) {
                    localResult = localResult + xSideLocal[i];
                }
            }

            isElements4 = true;

            // Add to result current group
            result = result + " + " + localResult;

            // Check current indexes
            checkedIndexes[0][col] = 1;
            checkedIndexes[1][col] = 1;
            checkedIndexes[2][col] = 1;
            checkedIndexes[3][col] = 1;
        }

        // Case 3 row ++ column ++
        else if (workArray[row][col] == 1 && workArray[row][(col + 1) % 4] == 1 &&
                 workArray[(row + 1) % 4][col] == 1 && workArray[(row + 1) % 4][(col + 1) % 4] == 1) {

            String[] xSideLocal = { "x1", "x3", "x1'", "x3'" };
            String[] ySideLocal = { "x2", "x4", "x2'", "x4'" };

            for (int i = 0; i <= 2; i += 2) {
                if (col == i) {
                    localResult = localResult + xSideLocal[i];
                }
            }

            for (int i = 0; i <= 2; i += 2) {
                if (row == i) {
                    localResult = localResult + ySideLocal[i];
                }
            }

            for (int i = 1; i <= 3; i += 2) {
                if (col == i) {
                    localResult = localResult + xSideLocal[i];
                }
            }

            for (int i = 1; i <= 3; i += 2) {
                if (row == i) {
                    localResult = localResult + ySideLocal[i];
                }
            }

            isElements4 = true;

            // Add to result current group
            result = result + " + " + localResult;

            // Check current indexes
            checkedIndexes[row][col] = 1;
            checkedIndexes[row][(col + 1) % 4] = 1;
            checkedIndexes[(row + 1) % 4][col] = 1;
            checkedIndexes[(row + 1) % 4][(col + 1) % 4] = 1;
        }

        // Case 4 row ++ column --
        else if (workArray[row][col] == 1 && workArray[row][(4 + (col - 1)) % 4] == 1 &&
                 workArray[(row + 1) % 4][col] == 1 && workArray[(row + 1) % 4][(4 + (col - 1)) % 4] == 1) {

            String[] xSideLocal = { "x3'", "x1", "x3", "x1'" };
            String[] ySideLocal = { "x2", "x4", "x2'", "x4'" };

            for (int i = 1; i <= 3; i += 2) {
                if (col == i) {
                    localResult = localResult + xSideLocal[i];
                }
            }

            for (int i = 0; i <= 2; i += 2) {
                if (row == i) {
                    localResult = localResult + ySideLocal[i];
                }
            }

            for (int i = 0; i <= 2; i += 2) {
                if (col == i) {
                    localResult = localResult + xSideLocal[i];
                }
            }

            for (int i = 1; i <= 3; i += 2) {
                if (row == i) {
                    localResult = localResult + ySideLocal[i];
                }
            }

            isElements4 = true;

            // Add to result current group
            result = result + " + " + localResult;

            // Check current indexes
            checkedIndexes[row][col] = 1;
            checkedIndexes[row][(4 + (col - 1)) % 4] = 1;
            checkedIndexes[(row + 1) % 4][col] = 1;
            checkedIndexes[(row + 1) % 4][(4 + (col - 1)) % 4] = 1;
        }

        // Case 5 row -- and column --
        else if (workArray[(4 + (row - 1)) % 4][(4 + (col - 1)) % 4] == 1 && workArray[(4 + (row - 1)) % 4][col] == 1 &&
                 workArray[row][(4 + (col - 1)) % 4] == 1 && workArray[row][col] == 1) {

            String[] xSideLocal = { "x3'", "x1", "x3", "x1'" };
            String[] ySideLocal = { "x4'", "x2", "x4", "x2'" };

            for (int i = 1; i <= 3; i += 2) {
                if (col == i) {
                    localResult = localResult + xSideLocal[i];
                }
            }

            for (int i = 1; i <= 3; i += 2) {
                if (row == i) {
                    localResult = localResult + ySideLocal[i];
                }
            }

            for (int i = 0; i <= 2; i += 2) {
                if (col == i) {
                    localResult = localResult + xSideLocal[i];
                }
            }

            for (int i = 0; i <= 2; i += 2) {
                if (row == i) {
                    localResult = localResult + ySideLocal[i];
                }
            }

            isElements4 = true;

            // Add to result current group
            result = result + " + " + localResult;

            // Check current indexes
            checkedIndexes[(4 + (row - 1)) % 4][(4 + (col - 1)) % 4] = 1;
            checkedIndexes[(4 + (row - 1)) % 4][col] = 1;
            checkedIndexes[row][(4 + (col - 1)) % 4] = 1;
            checkedIndexes[row][col] = 1;
        }

        // Case 6 row -- column ++
        else if (workArray[(4 + (row - 1)) % 4][(col + 1) % 4] == 1 && workArray[(4 + (row - 1)) % 4][col] == 1 &&
                 workArray[row][(col + 1) % 4] == 1 && workArray[row][col] == 1) {

            String[] xSideLocal = { "x1", "x3", "x1'", "x3'" };
            String[] ySideLocal = { "x4'", "x2", "x4", "x2'" };

            for (int i = 0; i <= 2; i += 2) {
                if (col == i) {
                    localResult = localResult + xSideLocal[i];
                }
            }

            for (int i = 1; i <= 3; i += 2) {
                if (row == i) {
                    localResult = localResult + ySideLocal[i];
                }
            }

            for (int i = 1; i <= 3; i += 2) {
                if (col == i) {
                    localResult = localResult + xSideLocal[i];
                }
            }

            for (int i = 0; i <= 2; i += 2) {
                if (row == i) {
                    localResult = localResult + ySideLocal[i];
                }
            }

            isElements4 = true;

            // Add to result current group
            result = result + " + " + localResult;

            // Check current indexes
            checkedIndexes[(4 + (row - 1)) % 4][col] = 1;
            checkedIndexes[(4 + (row - 1)) % 4][(col + 1) % 4] = 1;
            checkedIndexes[row][col] = 1;
            checkedIndexes[row][(col + 1) % 4] = 1;
        }

        return isElements4;
    }

    static boolean check2(int row, int col) {
        boolean isElements2 = false;
        String localResult = "";

        // Case 1 column ++
        if (workArray[row][col] == 1 && workArray[row][(col + 1) % 4] == 1) {

            String[] x2Values = { "x2", "x2", "x2'", "x2'" };
            String[] x4Values = { "x4'", "x4", "x4", "x4'" };
            String[] x1x3Values = { "x1", "x3", "x1'", "x3'" };

            for (int i = 0; i <= 2; i += 2) {
                if (col == i) {
                    localResult = localResult + x1x3Values[i];
                }
            }

            for (int i = 0; i < 4; i++) {
                if (row == i) {
                    localResult = localResult + x2Values[i];
                }
            }

            for (int i = 1; i <= 3; i += 2) {
                if (col == i) {
                    localResult = localResult + x1x3Values[i];
                }
            }

            for (int i = 0; i < 4; i++) {
                if (row == i) {
                    localResult = localResult + x4Values[i];
                }
            }

            isElements2 = true;

            // Add to result current group
            result = result + " + " + localResult;

            // Check current indexes
            checkedIndexes[row][col] = 1;
            checkedIndexes[row][(col + 1) % 4] = 1;

        }

        // Case 2 column --
        else if (workArray[row][(4 + (col - 1)) % 4] == 1 && workArray[row][col] == 1) {

            String[] x2Values = { "x2", "x2", "x2'", "x2'" };
            String[] x4Values = { "x4'", "x4", "x4", "x4'" };
            String[] x1x3Values = { "x3'", "x1", "x3", "x1'" };

            for (int i = 1; i <= 3; i += 2) {
                if (col == i) {
                    localResult = localResult + x1x3Values[i];
                }
            }

            for (int i = 0; i < 4; i++) {
                if (row == i) {
                    localResult = localResult + x2Values[i];
                }
            }

            for (int i = 0; i <= 2; i += 2) {
                if (col == i) {
                    localResult = localResult + x1x3Values[i];
                }
            }

            for (int i = 0; i < 4; i++) {
                if (row == i) {
                    localResult = localResult + x4Values[i];
                }
            }

            isElements2 = true;

            // Add to result current group
            result = result + " + " + localResult;

            // Check current indexes
            checkedIndexes[row][(4 + (col - 1)) % 4] = 1;
            checkedIndexes[row][col] = 1;
        }

        // Case 3 row ++
        else if (workArray[row][col] == 1 && workArray[(row + 1) % 4][col] == 1) {

            String[] x1Values = { "x1", "x1", "x1'", "x1'" };
            String[] x3Values = { "x3'", "x3", "x3", "x3'" };
            String[] x2x4Values = { "x2", "x4", "x2'", "x4'" };

            for (int i = 0; i < 4; i++) {
                if (col == i) {
                    localResult = localResult + x1Values[i];
                }
            }

            for (int i = 0; i <= 2; i += 2) {
                if (row == i) {
                    localResult = localResult + x2x4Values[i];
                }
            }

            for (int i = 0; i < 4; i++) {
                if (col == i) {
                    localResult = localResult + x3Values[i];
                }
            }

            for (int i = 1; i <= 3; i += 2) {
                if (row == i) {
                    localResult = localResult + x2x4Values[i];
                }
            }

            isElements2 = true;

            // Add to result current group
            result = result + " + " + localResult;

            // Check current indexes
            checkedIndexes[row][col] = 1;
            checkedIndexes[(row + 1) % 4][col] = 1;
        }

        // Case 4 row --
        else if (workArray[row][col] == 1 && workArray[(4 + (row - 1)) % 4][col] == 1) {

            String[] x1Values = { "x1", "x1", "x1'", "x1'" };
            String[] x3Values = { "x3'", "x3", "x3", "x3'" };
            String[] x2x4Values = { "x4'", "x2", "x4", "x2'" };

            for (int i = 0; i < 4; i++) {
                if (col == i) {
                    localResult = localResult + x1Values[i];
                }
            }

            for (int i = 1; i <= 3; i += 2) {
                if (row == i) {
                    localResult = localResult + x2x4Values[i];
                }
            }

            for (int i = 0; i < 4; i++) {
                if (col == i) {
                    localResult = localResult + x3Values[i];
                }
            }

            for (int i = 0; i <= 2; i += 2) {
                if (row == i) {
                    localResult = localResult + x2x4Values[i];
                }
            }

            isElements2 = true;

            // Add to result current group
            result = result + " + " + localResult;

            // Check current indexes
            checkedIndexes[row][col] = 1;
            checkedIndexes[(4 + (row - 1)) % 4][col] = 1;
        }

        return isElements2;
    }

    static boolean check1(int row, int col) {
        boolean isElements1 = false;
        String localResult = "";

        String[] x1Values = { "x1", "x1'" };
        String[] x2Values = { "x2", "x2'" };
        String[] x3Values = { "x3", "x3'" };
        String[] x4Values = { "x4", "x4'" };

        if (col == 0 || col == 1) {
            localResult = localResult + x1Values[0];
            isElements1 = true;
        } else {
            localResult = localResult + x1Values[1];
            isElements1 = true;
        }

        if (row == 0 || row == 1) {
            localResult = localResult + x2Values[0];
            isElements1 = true;
        } else {
            localResult = localResult + x2Values[1];
            isElements1 = true;
        }

        if (col == 1 || col == 2) {
            localResult = localResult + x3Values[0];
            isElements1 = true;
        } else {
            localResult = localResult + x3Values[1];
            isElements1 = true;
        }

        if (row == 1 || row == 2) {
            localResult = localResult + x4Values[0];
            isElements1 = true;
        } else {
            localResult = localResult + x4Values[1];
            isElements1 = true;
        }

        // Add to result current group
        result = result + " + " + localResult;

        // Check current indexes
        checkedIndexes[row][col] = 1;

        return isElements1;
    }

    static boolean check0() {
        boolean isElements0 = false;
        int counterForZero = 0;
        String localResult = "0";

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (workArray[i][j] == 0) {
                    counterForZero++;
                }
            }
        }

        if (counterForZero == 16) {
            result = localResult;
            isElements0 = true;
        }

        return isElements0;
    }

    static void initialize() {
        int counterForValues = 0;

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                workArray[i][j] = valuesTable[counterForValues];
                checkedIndexes[i][j] = 0;
                counterForValues++;
            }
        }
    }

    public static void main(String[] args) {
        @SuppressWarnings("unused")
        Veitch map = new Veitch();
    }
}