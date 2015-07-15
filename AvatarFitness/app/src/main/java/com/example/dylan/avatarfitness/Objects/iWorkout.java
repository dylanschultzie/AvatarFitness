package com.example.dylan.avatarfitness.Objects;

import java.util.Date;

/**@author Dylan Schultz
 * Date Created: 2/16/2015
 * Date Last Revised: 7/14/2015
 *
 * Purpose:
 *      Interface for both Run and Workout classes. Requires base functionality in order to
 *      better support polymorphism.
 *
 */
public interface iWorkout {
    String getDescription();
    float getDuration();
    Date getDate();
}
