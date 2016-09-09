package com.github.ybq.parallaxviewpager;

/**
 * Author : ybq
 * Date :  15/8/15.
 */
public enum Mode {

  LEFT_OVERLAY(0), RIGHT_OVERLAY(1), NONE(2);
  int value;

  Mode(int value) {
    this.value = value;
  }
}
