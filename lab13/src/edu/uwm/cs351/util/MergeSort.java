package edu.uwm.cs351.util;

import java.lang.reflect.Array;

public class MergeSort {

    @SuppressWarnings("unchecked")
    public static <E extends Comparable<E>> E[] sort(E[] original) {
        if (original.length < 2)
            return original;

        E[] left = (E[]) Array.newInstance(original.getClass().getComponentType(), original.length / 2);
        for (int i = 0; i < left.length; i++)
            left[i] = original[i];
        left= sort(left);
        E[] right = (E[]) Array.newInstance(original.getClass().getComponentType(), original.length - left.length);
        for (int i = 0; i < right.length; i++)
            right[i] = original[left.length + i];
        right=sort(right);
        return merge(left, right);
        // TODO: complete the MergeSort
    }

    @SuppressWarnings("unchecked")
    static <E extends Comparable<E>> E[] merge(E[] left, E[] right) {
        E[] out = (E[]) Array.newInstance(left.getClass().getComponentType(), left.length + right.length);

        int i = 0, j = 0, k = 0;

		// TODO: merge two sorted arrays into `out` array such that result is also sorted
        while (out.length!=i) {
        	if (k>=right.length || j<left.length && left[j].compareTo(right[k])<=0) {
        		out[i]=left[j];
        		++j; ++i;
        	} else {
        		out[i]=right[k];
        		++k; ++i;
        	}
        }

        return out;
    }
}
