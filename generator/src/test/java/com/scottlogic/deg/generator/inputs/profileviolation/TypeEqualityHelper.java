package com.scottlogic.deg.generator.inputs.profileviolation;

import com.scottlogic.deg.common.profile.Profile;
import com.scottlogic.deg.common.profile.Rule;
import com.scottlogic.deg.common.profile.constraints.Constraint;
import com.scottlogic.deg.common.profile.constraints.atomic.NotConstraint;
import com.scottlogic.deg.common.profile.constraints.grammatical.AndConstraint;
import com.scottlogic.deg.common.profile.constraints.grammatical.ConditionalConstraint;
import com.scottlogic.deg.common.profile.constraints.grammatical.OrConstraint;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;

import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;

/**
 * Provides methods for asserting deep equality between two profile lists.
 */
public class TypeEqualityHelper {

    /**
     * Asserts that the two given profile lists are equivalent in both shape (sameBeanAs) and type.
     * @param actualProfileList Actual list of profiles.
     * @param expectedProfileList Expected list of profiles.
     */
    public static void assertProfileListsAreEquivalent(List<Profile> actualProfileList,
                                                       List<Profile> expectedProfileList) {
        assertThat(
            "The lists of profiles do not serialize to the same values. Use the \"<Click to see difference> option\"",
            actualProfileList,
            sameBeanAs(expectedProfileList)
        );
        assertListProfileTypeEquality(actualProfileList, expectedProfileList);
    }

    /**
     * Asserts all profiles in two lists have constraints and all sub-constraints of the same type.
     * Note that the lists will be the same length as shown by the sameBeanAs() assertion above.
     * @param expectedProfileList The expected profile list.
     * @param actualProfileList The actual profile list.
     */
    public static void assertListProfileTypeEquality(List<Profile> expectedProfileList, List<Profile> actualProfileList) {
        Assert.assertEquals("Profile list lengths do not match. Expected: " + expectedProfileList.size()
            + ", Actual: " + actualProfileList.size(),
            expectedProfileList.size(),
            actualProfileList.size()
        );
        for (int i = 0; i < expectedProfileList.size(); i++) {
            assertProfileTypeEquality(expectedProfileList.get(i), actualProfileList.get(i));
        }
    }

    /**
     * Asserts all rules within two profiles have constraints and all sub-constraints of the same type.
     * Note that the lists will be the same length as shown by the sameBeanAs() assertion above.
     * @param expectedProfile The expected profile.
     * @param actualProfile The actual profile.
     */
    public static void assertProfileTypeEquality(Profile expectedProfile, Profile actualProfile) {
        ArrayList<Rule> expectedRules = new ArrayList<>(expectedProfile.getRules());
        ArrayList<Rule> actualRules = new ArrayList<>(actualProfile.getRules());
        Assert.assertEquals("Rule list lengths do not match. Expected: " + expectedRules.size()
                + ", Actual: " + actualRules.size(),
            expectedRules.size(),
            actualRules.size()
        );
        for (int i = 0; i < expectedRules.size(); i++) {
            assertRuleTypeEquality(expectedRules.get(i), actualRules.get(i));
        }
    }

    /**
     * Asserts all constraints in two rules have the same type and all sub-constraints of these constraints have the
     * same type.
     * @param expectedRule The expected rule.
     * @param actualRule The actual rule.
     */
    public static void assertRuleTypeEquality(Rule expectedRule, Rule actualRule) {
        ArrayList<Constraint> expectedConstraints = new ArrayList<>(expectedRule.constraints);
        ArrayList<Constraint> actualConstraints = new ArrayList<>(actualRule.constraints);
        assertConstraintListTypeEquality(expectedConstraints, actualConstraints);
    }

    /**
     * Asserts all constraints in two lists of constraints and all sub-constraints have the same type.
     * Note that the lists will be the same length as shown by the sameBeanAs() assertion above.
     * @param expectedConstraintList The expected list of constraints.
     * @param actualConstraintList The actual list of constraints.
     */
    public static void assertConstraintListTypeEquality(List<Constraint> expectedConstraintList,
                                                        List<Constraint> actualConstraintList) {
        Assert.assertEquals("Constraint list lengths do not match. Expected: " + expectedConstraintList.size()
                + ", Actual: " + actualConstraintList.size(),
            expectedConstraintList.size(),
            actualConstraintList.size()
        );
        for (int i = 0; i < expectedConstraintList.size(); i++) {
            assertConstraintTypeEquality(expectedConstraintList.get(i), actualConstraintList.get(i));
        }
    }

    /**
     * Asserts deep type equality for constraints that can contain other constraints.
     * For example asserts that an AND(OR(X,Y),OR(Z,W)) is not the same as AND(OR(X,Y),AND(Z,W))
     * but cannot distinguish between constraints A:(foo less than 10) and B:(bar less than 50)
     * Note that the lists will be the same length as shown by the sameBeanAs() assertion above.
     * @param expectedConstraint The expected constraint.
     * @param actualConstraint The constraint under test.
     */
    public static void assertConstraintTypeEquality(Constraint expectedConstraint, Constraint actualConstraint) {
        if (expectedConstraint == null && actualConstraint == null) {
            return;
        }
        Assert.assertEquals("Class types do not match for constraints. Expected: "
                + expectedConstraint.getClass() + " but was: " + actualConstraint.getClass(),
            expectedConstraint.getClass(),
            actualConstraint.getClass());
        if (expectedConstraint instanceof NotConstraint){
            assertConstraintTypeEquality(
                ((NotConstraint)expectedConstraint).negatedConstraint,
                ((NotConstraint)actualConstraint).negatedConstraint);
        }
        else if (expectedConstraint instanceof AndConstraint) {
            ArrayList<Constraint> expectedConstraints = new ArrayList<>(((AndConstraint) expectedConstraint).subConstraints);
            ArrayList<Constraint> actualConstraints = new ArrayList<>(((AndConstraint) actualConstraint).subConstraints);
            assertConstraintListTypeEquality(expectedConstraints, actualConstraints);
        }
        else if (expectedConstraint instanceof OrConstraint) {
            ArrayList<Constraint> expectedConstraints = new ArrayList<>(((OrConstraint) expectedConstraint).subConstraints);
            ArrayList<Constraint> actualConstraints = new ArrayList<>(((OrConstraint) actualConstraint).subConstraints);
            assertConstraintListTypeEquality(expectedConstraints, actualConstraints);
        }
        else if (expectedConstraint instanceof ConditionalConstraint) {
            ConditionalConstraint expectedConditionalConstraint = (ConditionalConstraint) expectedConstraint;
            ConditionalConstraint actualConditionalConstraint = (ConditionalConstraint) actualConstraint;
            assertConstraintTypeEquality(
                expectedConditionalConstraint.condition,
                actualConditionalConstraint.condition);
            assertConstraintTypeEquality(
                expectedConditionalConstraint.whenConditionIsFalse,
                actualConditionalConstraint.whenConditionIsFalse);
            assertConstraintTypeEquality(
                expectedConditionalConstraint.whenConditionIsTrue,
                actualConditionalConstraint.whenConditionIsTrue);
        }
    }
}
