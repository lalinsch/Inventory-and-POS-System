import org.hyperskill.hstest.dynamic.DynamicTest;
import org.hyperskill.hstest.stage.SwingTest;
import org.hyperskill.hstest.testcase.CheckResult;
import org.hyperskill.hstest.testing.swing.SwingComponent;
import winemerchant.GUI.RootWindow;
import org.assertj.swing.fixture.*;
import static org.hyperskill.hstest.testing.expect.Expectation.expect;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hyperskill.hstest.testcase.CheckResult.correct;
import static org.hyperskill.hstest.testcase.CheckResult.wrong;

public class WineMerchantTest extends SwingTest {

    public WineMerchantTest() {
        super(new RootWindow());
    }

    @SwingComponent JPanelFixture topPanel;
    @SwingComponent JPanelFixture middlePanel;
    @SwingComponent JPanelFixture bottomPanel;

    @SwingComponent JLabelFixture instructionLabel;
    @SwingComponent JRadioButtonFixture merlotButton;
    @SwingComponent JRadioButtonFixture roseButton;
    @SwingComponent JRadioButtonFixture sauvignonButton;

    @SwingComponent JComboBoxFixture amountComboBox;
    @SwingComponent JComboBoxFixture supplierComboBox;
    @SwingComponent JTextComponentFixture purchasedPriceTextField;

    @SwingComponent JLabelFixture messageLabel;
    @SwingComponent JButtonFixture submitButton;
    @SwingComponent JLabelFixture successLabel;

    @DynamicTest(feedback = "SubmitButton should be disabled.")
    CheckResult test1() {
        requireVisible(instructionLabel);
        requireVisible(topPanel);
        requireEnabled(merlotButton);
        requireEnabled(roseButton);
        requireEnabled(sauvignonButton);
        requireVisible(bottomPanel);
        requireNotVisible(middlePanel);
        requireNotVisible(successLabel);
        requireDisabled(submitButton);
        return correct();
    }

    @DynamicTest (feedback = "MiddlePanel should be visible after clicking on a wine type")
    CheckResult test2() {
        merlotButton.click();
        requireVisible(middlePanel);
        requireNotVisible(successLabel);
        requireEnabled(submitButton);
        return correct();
    }

    @DynamicTest (feedback = "Adding a valid name, purchased price amount should make SuccessLabel become visible")
    CheckResult test3() {
        supplierComboBox.enterText("Saba");
        amountComboBox.selectItem(2);
        purchasedPriceTextField.setText("845");
        submitButton.click();
        successLabel.requireVisible();
        return correct();
    }

    @DynamicTest (feedback = "The program should check that a valid name and purchased price amount has been entered" +
            "\n MessageLabel should include the word 'error'")
    CheckResult test4() {
        Pattern p = Pattern.compile("^.*[E|e]rror.*$");
        roseButton.click();
        amountComboBox.selectItem(0);
        supplierComboBox.enterText("S@ba");
        purchasedPriceTextField.setText("450");
        submitButton.click();
        messageLabel.requireText(p);
        supplierComboBox.enterText("Saba");
        purchasedPriceTextField.setText("45b");
        submitButton.click();
        messageLabel.requireText(p);
        return correct();
    }

    @DynamicTest (feedback = "When selecting 4 cases, the success message should include the String '48' confirming" +
            "that 4 cases have successfully been added")
    CheckResult test5() {
        Pattern p = Pattern.compile("^.*48.*$");
        sauvignonButton.click();
        amountComboBox.selectItem(3);
        supplierComboBox.enterText("Tom");
        purchasedPriceTextField.setText("4515.15");
        submitButton.click();
        messageLabel.requireText(p);
        successLabel.requireVisible();
        return correct();
    }


}
