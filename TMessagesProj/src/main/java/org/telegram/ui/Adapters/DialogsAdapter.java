/*
 * This is the source code of Telegram for Android v. 3.x.x.
 * It is licensed under GNU GPL v. 2 or later.
 * You should have received a copy of the license in this archive (see LICENSE).
 *
 * Copyright Nikolai Kudashov, 2013-2017.
 */

package org.telegram.ui.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import org.fullgram.messenger.AndroidUtilities;
import org.fullgram.messenger.MessagesController;
import org.fullgram.messenger.support.widget.RecyclerView;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.Cells.DialogCell;
import org.telegram.ui.Cells.LoadingCell;
import org.telegram.ui.Components.RecyclerListView;

import java.util.ArrayList;

public class DialogsAdapter extends RecyclerListView.SelectionAdapter {

    private Context mContext;
    private int dialogsType;
    private long openedDialogId;
    private int currentCount;

    public DialogsAdapter(Context context, int type) {
        mContext = context;
        dialogsType = type;
    }

    public void setOpenedDialogId(long id) {
        openedDialogId = id;
    }

    public boolean isDataSetChanged() {
        int current = currentCount;
        return current != getItemCount() || current == 1;
    }

    private ArrayList<TLRPC.TL_dialog> getDialogsArray() {
        if (dialogsType == 0) {
            return MessagesController.getInstance().dialogs;
        } else if (dialogsType == 1) {
            return MessagesController.getInstance().dialogsServerOnly;
        } else if (dialogsType == 2) {
            return MessagesController.getInstance().dialogsGroupsOnly;
        }
        return null;
    }

    @Override
    public int getItemCount() {
        int count = getDialogsArray().size();
        if (count == 0 && MessagesController.getInstance().loadingDialogs) {
            return 0;
        }
        if (!MessagesController.getInstance().dialogsEndReached) {
            count++;
        }
        currentCount = count;
        return count;
    }

    public TLRPC.TL_dialog getItem(int i) {
        ArrayList<TLRPC.TL_dialog> arrayList = getDialogsArray();
        if (i < 0 || i >= arrayList.size()) {
            return null;
        }
        return arrayList.get(i);
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        if (holder.itemView instanceof DialogCell) {
            ((DialogCell) holder.itemView).checkCurrentDialogIndex();
        }
    }

    @Override
    public boolean isEnabled(RecyclerView.ViewHolder holder) {
        return holder.getItemViewType() != 1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = null;
        if (viewType == 0) {
            view = new DialogCell(mContext);
        } else if (viewType == 1) {
            view = new LoadingCell(mContext);
        }
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        return new RecyclerListView.Holder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder.getItemViewType() == 0) {
            DialogCell cell = (DialogCell) viewHolder.itemView;
            cell.useSeparator = (i != getItemCount() - 1);
            TLRPC.TL_dialog dialog = getItem(i);
            if (dialogsType == 0) {
                if (AndroidUtilities.isTablet()) {
                    cell.setDialogSelected(dialog.id == openedDialogId);
                }
            }
            cell.setDialog(dialog, i, dialogsType);
        }
    }

    @Override
    public int getItemViewType(int i) {
        if (i == getDialogsArray().size()) {
            return 1;
        }
        return 0;
    }
}
