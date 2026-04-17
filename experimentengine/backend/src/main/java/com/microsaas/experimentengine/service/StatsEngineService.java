package com.microsaas.experimentengine.service;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.TDistribution;
import org.springframework.stereotype.Service;

@Service
public class StatsEngineService {

    public double calculateProportionPValue(int n1, int x1, int n2, int x2) {
        if (n1 == 0 || n2 == 0) return 1.0;

        double p1 = (double) x1 / n1;
        double p2 = (double) x2 / n2;
        double pPooled = (double) (x1 + x2) / (n1 + n2);

        double se = Math.sqrt(pPooled * (1 - pPooled) * (1.0 / n1 + 1.0 / n2));
        if (se == 0) return 1.0;

        double z = (p2 - p1) / se;
        NormalDistribution normalDist = new NormalDistribution(0, 1);

        // Two-tailed p-value
        return 2 * (1.0 - normalDist.cumulativeProbability(Math.abs(z)));
    }

    public double calculateContinuousPValue(double mean1, double var1, int n1, double mean2, double var2, int n2) {
        if (n1 <= 1 || n2 <= 1) return 1.0;

        double se1 = var1 / n1;
        double se2 = var2 / n2;
        double se = Math.sqrt(se1 + se2);
        if (se == 0) return 1.0;

        double t = (mean2 - mean1) / se;

        // Welch-Satterthwaite equation for degrees of freedom
        double df = Math.pow(se1 + se2, 2) /
                    (Math.pow(se1, 2) / (n1 - 1) + Math.pow(se2, 2) / (n2 - 1));

        TDistribution tDist = new TDistribution(df);

        // Two-tailed p-value
        return 2 * (1.0 - tDist.cumulativeProbability(Math.abs(t)));
    }

    public double calculateBayesianProbBetter(int alpha1, int beta1, int alpha2, int beta2) {
        // Approximate Bayesian probability P(B > A) for Beta distributions using normal approximation
        // Mean and Variance of Beta distribution
        double mean1 = (double) alpha1 / (alpha1 + beta1);
        double var1 = (double) (alpha1 * beta1) / (Math.pow(alpha1 + beta1, 2) * (alpha1 + beta1 + 1));

        double mean2 = (double) alpha2 / (alpha2 + beta2);
        double var2 = (double) (alpha2 * beta2) / (Math.pow(alpha2 + beta2, 2) * (alpha2 + beta2 + 1));

        double diffMean = mean2 - mean1;
        double diffVar = var1 + var2;

        if (diffVar == 0) return mean2 > mean1 ? 1.0 : 0.0;

        double z = diffMean / Math.sqrt(diffVar);
        NormalDistribution normalDist = new NormalDistribution(0, 1);

        return normalDist.cumulativeProbability(z);
    }
}
