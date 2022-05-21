package org.metatrans.commons.graphics2d.model.entities;


import android.graphics.Bitmap;
import android.graphics.RectF;

import org.metatrans.commons.graphics2d.model.IWorld;
import org.metatrans.commons.ui.images.BitmapCacheBase;
import org.metatrans.commons.ui.utils.BitmapUtils;


public class Entity2D_Feeding extends Entity2D_Ground {


	private static final long serialVersionUID = -4487276789823271266L;

	private Bitmap bitmap1;
	private Bitmap bitmap2;
	private int width;
	private int height;
	private Bitmap bitmap_final;

	public Entity2D_Feeding(IWorld world, RectF _envelop1, Bitmap _bitmap1, RectF _envelop2, Bitmap _bitmap2) {

		super(world, _envelop1, IEntity2D.SUBTYPE_GROUND_FEEDING, -1, -1);

		width = (int) (_envelop1.right - _envelop1.left);
		height = (int) (_envelop1.bottom - _envelop1.top);

		bitmap1 = _bitmap1;
		bitmap2 = _bitmap2;
	}


	@Override
	public int getType() {

		return IEntity2D.TYPE_GROUND;
	}

	@Override
	public Bitmap getBitmap() {

		if (bitmap_final == null) {

			bitmap_final = BitmapUtils.combineImages_Overlap(bitmap1, bitmap2, width, height);
		}

		return bitmap_final;
	}
}
