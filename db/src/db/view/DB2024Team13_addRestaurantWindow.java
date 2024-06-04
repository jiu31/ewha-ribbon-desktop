package db.view;

import db.model.DB2024Team13_connection;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DB2024Team13_addRestaurantWindow {
	
	
	//가게 추가 dialog를 표시
    public static boolean showAddRestaurantDialog(Component parentComponent) {
    	//dialog 생성 및 설정
        JDialog dialog = createDialog(parentComponent);
        GridBagConstraints gbc = createGridBagConstraints();
        
        
        //가게 정보 입력 받는 field 및 comboBox 생성, dialog 추가
        JTextField nameField = new JTextField(15);
        JTextField locationField = new JTextField(15);
        JTextField menuField = new JTextField(15);
        JComboBox<String> categoryComboBox = new JComboBox<>(getCategories());
        JCheckBox breakTimeCheckBox = new JCheckBox();
        JComboBox<String> sectionComboBox = new JComboBox<>(getSections());
        JCheckBox eatAloneCheckBox = new JCheckBox();

        addLabelsToDialog(dialog, gbc);
        addFieldsToDialog(dialog, gbc, nameField, locationField, menuField, categoryComboBox, breakTimeCheckBox, sectionComboBox, eatAloneCheckBox);

        boolean[] success = {false};
        
        
        //가게추가 버튼인 "추가하기" 설정
        JButton submitButton = new JButton("추가하기");
        addSubmitButtonToDialog(dialog, gbc, submitButton, nameField, locationField, menuField, categoryComboBox, breakTimeCheckBox, sectionComboBox, eatAloneCheckBox, success);

        dialog.setVisible(true);
        return success[0];
    }
    
    
    //dialog 창 기본 설정
    private static JDialog createDialog(Component parentComponent) {
        JDialog dialog = new JDialog((Frame) null, "가게 추가", true);
        dialog.setSize(400, 500);
        dialog.setLocationRelativeTo(parentComponent);
        dialog.setLayout(new GridBagLayout());
        return dialog;
    }
    
    
    //dialog 그리드 설정
    private static GridBagConstraints createGridBagConstraints() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        return gbc;
    }
    
    
    //레이블 및 입력필드 추가
    private static void addLabelsToDialog(JDialog dialog, GridBagConstraints gbc) {
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridx = 0;
        gbc.gridy = 0;
        dialog.add(new JLabel("가게 이름:"), gbc);
        gbc.gridy++;
        dialog.add(new JLabel("위치:"), gbc);
        gbc.gridy++;
        dialog.add(new JLabel("대표 메뉴:"), gbc);
        gbc.gridy++;
        dialog.add(new JLabel("카테고리:"), gbc);
        gbc.gridy++;
        dialog.add(new JLabel("브레이크 타임:"), gbc);
        gbc.gridy++;
        dialog.add(new JLabel("섹션 이름:"), gbc);
        gbc.gridy++;
        dialog.add(new JLabel("혼밥 가능:"), gbc);
    }

    private static void addFieldsToDialog(JDialog dialog, GridBagConstraints gbc, JTextField nameField, JTextField locationField,
                                          JTextField menuField, JComboBox<String> categoryComboBox, JCheckBox breakTimeCheckBox,
                                          JComboBox<String> sectionComboBox, JCheckBox eatAloneCheckBox) {
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 1;
        gbc.gridy = 0;
        dialog.add(nameField, gbc);
        gbc.gridy++;
        dialog.add(locationField, gbc);
        gbc.gridy++;
        dialog.add(menuField, gbc);
        gbc.gridy++;
        dialog.add(categoryComboBox, gbc);
        gbc.gridy++;
        dialog.add(breakTimeCheckBox, gbc);
        gbc.gridy++;
        dialog.add(sectionComboBox, gbc);
        gbc.gridy++;
        dialog.add(eatAloneCheckBox, gbc);
    }

    
    //"추가하기" 버튼에 대한 액션 및 트랜잭션 추가
    private static void addSubmitButtonToDialog(JDialog dialog, GridBagConstraints gbc, JButton submitButton, JTextField nameField,
                                                JTextField locationField, JTextField menuField, JComboBox<String> categoryComboBox,
                                                JCheckBox breakTimeCheckBox, JComboBox<String> sectionComboBox, JCheckBox eatAloneCheckBox,
                                                boolean[] success) {
        gbc.gridx = 1;
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.CENTER;
        dialog.add(submitButton, gbc);
        
        //"추가하기"버튼에 대한 액션
        submitButton.addActionListener(e -> {
        	//유효성 검사
            if (areFieldsValid(nameField, locationField, menuField, categoryComboBox, sectionComboBox)) {
            	//'가게 추가'에 대한 트랜잭션
            	success[0] = addRestaurantWithTransaction(
                        nameField.getText(),
                        locationField.getText(),
                        menuField.getText(),
                        (String) categoryComboBox.getSelectedItem(),
                        breakTimeCheckBox.isSelected(),
                        (String) sectionComboBox.getSelectedItem(),
                        eatAloneCheckBox.isSelected()
                );
                handleSubmissionResult(dialog, success[0]);
            } else {
            	//필드 유효성 검사 실패 시 오류 메시지 출력
                showErrorMessage(dialog, "모든 필드를 입력해주세요.");
            }
        });
    }
    
    //유효성 검사
    private static boolean areFieldsValid(JTextField nameField, JTextField locationField, JTextField menuField,
                                          JComboBox<String> categoryComboBox, JComboBox<String> sectionComboBox) {
        return !nameField.getText().isEmpty() &&
               !locationField.getText().isEmpty() &&
               !menuField.getText().isEmpty() &&
               categoryComboBox.getSelectedItem() != null &&
               sectionComboBox.getSelectedItem() != null;
    }
    
    //'가게 추가'에 대한 결과 처리
    private static void handleSubmissionResult(JDialog dialog, boolean success) {
        if (success) {
            JOptionPane.showMessageDialog(dialog, "가게가 성공적으로 추가되었습니다.");
            dialog.dispose();
        } else {
            showErrorMessage(dialog, "가게 추가 실패. 데이터를 확인해주세요.");
        }
    }
    
    //오류 메세지
    private static void showErrorMessage(JDialog dialog, String message) {
        JOptionPane.showMessageDialog(dialog, message, "입력 오류", JOptionPane.ERROR_MESSAGE);
    }
    
    //데이터베이스에서 카테고리,섹션, 데이터 목록 가져옴
    private static String[] getCategories() {
        return getDataFromDatabase("SELECT DISTINCT category FROM DB2024_restaurant", "category");
    }

    private static String[] getSections() {
        return getDataFromDatabase("SELECT DISTINCT section FROM DB2024_section", "section");
    }

    private static String[] getDataFromDatabase(String query, String columnLabel) {
        List<String> dataList = new ArrayList<>();
        try (Connection conn = DB2024Team13_connection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                dataList.add(rs.getString(columnLabel));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataList.toArray(new String[0]);
    }
    
    //'가게 추가'에 대한 트랜잭션 설정
    private static boolean addRestaurantWithTransaction(String name, String location, String bestMenu, String category, boolean breakTime,
                                                        String sectionName, boolean eatAlone) {
        String restaurantSql = "INSERT INTO DB2024_restaurant (rest_name, location, best_menu, category, breaktime, section_name, eat_alone) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DB2024Team13_connection.getConnection();
             PreparedStatement restaurantStmt = conn.prepareStatement(restaurantSql)) {
            conn.setAutoCommit(false);

            restaurantStmt.setString(1, name);
            restaurantStmt.setString(2, location);
            restaurantStmt.setString(3, bestMenu);
            restaurantStmt.setString(4, category);
            restaurantStmt.setBoolean(5, breakTime);
            restaurantStmt.setString(6, sectionName);
            restaurantStmt.setBoolean(7, eatAlone);
            restaurantStmt.executeUpdate();
            
            //트랜잭션 커밋
            conn.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            rollbackTransaction();
            return false;
        }
    }
    
    //트랜잭션 롤백
    private static void rollbackTransaction() {
        try (Connection conn = DB2024Team13_connection.getConnection()) {
            if (conn != null) {
                conn.rollback();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
