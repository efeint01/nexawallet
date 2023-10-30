package com.app.nexawallet.utilities;

import android.os.Bundle;

import androidx.annotation.AnimRes;
import androidx.annotation.IdRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class FragmentUtils {
    public static void addFragmentWithAnimation(
            FragmentManager fragmentManager,
            @IdRes int containerViewId,
            Class<? extends Fragment> fragmentClass,
            @AnimRes int enterAnimation,
            @AnimRes int exitAnimation,
            @AnimRes int popEnterAnimation,
            @AnimRes int popExitAnimation,
            Bundle fragmentArgs) {

        Fragment fragment;
        try {
            fragment = fragmentClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Cannot instantiate the fragment.", e);
        }

        if (fragmentArgs != null) {
            fragment.setArguments(fragmentArgs);
        }

        if (!fragmentManager.isStateSaved()) {
            fragmentManager.beginTransaction()
                    .setCustomAnimations(enterAnimation, exitAnimation, popEnterAnimation, popExitAnimation)
                    .addToBackStack(null)
                    .add(containerViewId, fragment)
                    .commit();
        } else {
            fragmentManager.beginTransaction()
                    .setCustomAnimations(enterAnimation, exitAnimation, popEnterAnimation, popExitAnimation)
                    .addToBackStack(null)
                    .add(containerViewId, fragment)
                    .commitAllowingStateLoss();
        }

    }

    public static void addFragmentWithAnimation(
            FragmentManager fragmentManager,
            @IdRes int containerViewId,
            Class<? extends Fragment> fragmentClass,
            @AnimRes int enterAnimation,
            @AnimRes int exitAnimation,
            @AnimRes int popEnterAnimation,
            @AnimRes int popExitAnimation) {

        Fragment fragment;
        try {
            fragment = fragmentClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Cannot instantiate the fragment.", e);
        }


        if (!fragmentManager.isStateSaved()) {
            fragmentManager.beginTransaction()
                    .setCustomAnimations(enterAnimation, exitAnimation, popEnterAnimation, popExitAnimation)
                    .addToBackStack(null)
                    .add(containerViewId, fragment)
                    .commit();
        } else {
            fragmentManager.beginTransaction()
                    .setCustomAnimations(enterAnimation, exitAnimation, popEnterAnimation, popExitAnimation)
                    .addToBackStack(null)
                    .add(containerViewId, fragment)
                    .commitAllowingStateLoss();
        }

    }


    public static void addFragment(
            FragmentManager fragmentManager,
            @IdRes int containerViewId,
            Class<? extends Fragment> fragmentClass,
            Bundle fragmentArgs) {

        Fragment fragment;
        try {
            fragment = fragmentClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Cannot instantiate the fragment.", e);
        }

        if (fragmentArgs != null) {
            fragment.setArguments(fragmentArgs);
        }

        fragmentManager.beginTransaction()
                .addToBackStack(null)
                .add(containerViewId, fragment)
                .commit();
    }

    public static void addFragment(
            FragmentManager fragmentManager,
            @IdRes int containerViewId,
            Class<? extends Fragment> fragmentClass) {

        Fragment fragment;
        try {
            fragment = fragmentClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Cannot instantiate the fragment.", e);
        }

        fragmentManager.beginTransaction()
                .addToBackStack(null)
                .add(containerViewId, fragment)
                .commit();
    }


    public static void replaceFragment(
            FragmentManager fragmentManager,
            @IdRes int containerViewId,
            Class<? extends Fragment> fragmentClass,
            Bundle fragmentArgs) {

        Fragment fragment;
        try {
            fragment = fragmentClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Cannot instantiate the fragment.", e);
        }

        if (fragmentArgs != null) {
            fragment.setArguments(fragmentArgs);
        }

        if (!fragmentManager.isStateSaved()) {
            fragmentManager.beginTransaction()
                    .replace(containerViewId, fragment)
                    .addToBackStack(null)
                    .commit();
        } else {
            fragmentManager.beginTransaction()
                    .replace(containerViewId, fragment)
                    .addToBackStack(null)
                    .commitAllowingStateLoss();
        }
    }

    public static void replaceFragment(
            FragmentManager fragmentManager,
            @IdRes int containerViewId,
            Class<? extends Fragment> fragmentClass) {

        Fragment fragment;
        try {
            fragment = fragmentClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Cannot instantiate the fragment.", e);
        }


        if (!fragmentManager.isStateSaved()) {
            fragmentManager.beginTransaction()
                    .replace(containerViewId, fragment)
                    .addToBackStack(null)
                    .commit();
        } else {
            fragmentManager.beginTransaction()
                    .replace(containerViewId, fragment)
                    .addToBackStack(null)
                    .commitAllowingStateLoss();
        }

    }


    public static void replaceFragmentWithAnimation(
            FragmentManager fragmentManager,
            @IdRes int containerViewId,
            Class<? extends Fragment> fragmentClass,
            @AnimRes int enterAnim,
            @AnimRes int exitAnim,
            @AnimRes int popEnterAnim,
            @AnimRes int popExitAnim,
            Bundle fragmentArgs) {

        Fragment fragment;
        try {
            fragment = fragmentClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Cannot instantiate the fragment.", e);
        }

        if (fragmentArgs != null) {
            fragment.setArguments(fragmentArgs);
        }

        fragmentManager.beginTransaction()
                .setCustomAnimations(enterAnim, exitAnim, popEnterAnim, popExitAnim)
                .replace(containerViewId, fragment)
                .addToBackStack(null)
                .commit();
    }


    public static void replaceFragmentWithAnimation(
            FragmentManager fragmentManager,
            @IdRes int containerViewId,
            Class<? extends Fragment> fragmentClass,
            @AnimRes int enterAnim,
            @AnimRes int exitAnim,
            @AnimRes int popEnterAnim,
            @AnimRes int popExitAnim) {

        Fragment fragment;
        try {
            fragment = fragmentClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Cannot instantiate the fragment.", e);
        }

        fragmentManager.beginTransaction()
                .setCustomAnimations(enterAnim, exitAnim, popEnterAnim, popExitAnim)
                .replace(containerViewId, fragment)
                .addToBackStack(null)
                .commit();
    }

    public static void showBottomSheet(
            FragmentManager fragmentManager,
            Class<? extends BottomSheetDialogFragment> bottomSheetClass,
            Bundle args) {

        BottomSheetDialogFragment bottomSheet;
        try {
            bottomSheet = bottomSheetClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Cannot instantiate the bottom sheet.", e);
        }

        if (args != null) {
            bottomSheet.setArguments(args);
        }

        bottomSheet.show(fragmentManager, bottomSheet.getTag());
    }

    public static void showBottomSheet(
            FragmentManager fragmentManager,
            Class<? extends BottomSheetDialogFragment> bottomSheetClass) {

        BottomSheetDialogFragment bottomSheet;
        try {
            bottomSheet = bottomSheetClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Cannot instantiate the bottom sheet.", e);
        }

        bottomSheet.show(fragmentManager, bottomSheet.getTag());
    }


}
