package it.unisa.casper.refactor.manipulator;

import com.intellij.psi.PsiMember;
import com.intellij.psi.PsiModifier;
import com.intellij.refactoring.move.moveMembers.MoveMembersOptions;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;


public class MockMoveMembersOptions implements MoveMembersOptions {
        private final PsiMember[] mySelectedMembers;
        private final String myTargetClassName;
        private String myMemberVisibility = PsiModifier.PUBLIC;

        public MockMoveMembersOptions(String targetClassName, PsiMember[] selectedMembers) {
            mySelectedMembers = selectedMembers;
            myTargetClassName = targetClassName;
        }

        public MockMoveMembersOptions(String targetClassName, Collection<PsiMember> memberSet) {
            this(targetClassName, memberSet.toArray(PsiMember.EMPTY_ARRAY));
        }

        @Override
        public String getMemberVisibility() {
            return myMemberVisibility;
        }

        @Override
        public boolean makeEnumConstant() {
            return true;
        }

        public void setMemberVisibility(@Nullable String visibility) {
            myMemberVisibility = visibility;
        }

        @Override
        public PsiMember[] getSelectedMembers() {
            return mySelectedMembers;
        }

        @Override
        public String getTargetClassName() {
            return myTargetClassName;
        }
}

