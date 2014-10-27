/* Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pingan.pabank.activity.homepage.animation.shortcut.view;

import android.view.View;

/**
 * Interface for initiating a drag within a view or across multiple views.
 *
 */
public interface DragController {
    
    /**
     * Interface to receive notifications when a drag starts or stops
     */
    interface DragListener {
        
        /**
         * A drag has begun
         * 
         * @param v The view that is being dragged
         * @param source An object representing where the drag originated
         * @param info The data associated with the object that is being dragged
         * @param dragAction The drag action: either {@link DragController#DRAG_ACTION_MOVE}
         *        or {@link DragController#DRAG_ACTION_COPY}
         */
        void onDragStart(View v, DragSource source, Object info, int dragAction);
        
        /**
         * The drag has eneded
         */
        void onDragEnd();
    }
    
    /**
     * Indicates the drag is a move.
     * 表示拖动是移动
     */
    public static int DRAG_ACTION_MOVE = 0;

    /**
     * Indicates the drag is a copy.
     * 表示拖动是复制
     */
    public static int DRAG_ACTION_COPY = 1;

    /**
     * Starts a drag
     * 开始一次拖动
     * 
     * @param v The view that is being dragged 被拖动的View对象
     * @param source An object representing where the drag originated 
     * @param info The data associated with the object that is being dragged
     * @param dragAction The drag action: either {@link #DRAG_ACTION_MOVE} or
     *        {@link #DRAG_ACTION_COPY}
     */
    void startDrag(View v, DragSource source, Object info, int dragAction);
}
