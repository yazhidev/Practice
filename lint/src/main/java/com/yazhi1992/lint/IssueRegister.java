package com.yazhi1992.lint;

import com.android.tools.lint.client.api.IssueRegistry;
import com.android.tools.lint.detector.api.Issue;

import java.util.Arrays;
import java.util.List;

/**
 * Created by zengyazhi on 2018/1/3.
 */

public class IssueRegister extends IssueRegistry {
    @Override
    public List<Issue> getIssues() {
        return Arrays.asList(MyLogDetector.ISSUE, ToastDetector.ISSUE);
    }

}

