package com.yzc.repository.index;

import java.io.Serializable;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

public class OffsetPageRequest implements Pageable, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final int offset;
	private final int size;
	private final Sort sort;

	/**
	 * 
	 * @param size
	 * @param offset
	 */
	public OffsetPageRequest(int offset, int size) {

		this(offset, size, null);
	}

	/**
	 * 
	 * @param offset
	 * @param size
	 * @param direction
	 * @param properties
	 */
	public OffsetPageRequest(int offset, int size, Direction direction,
			String... properties) {

		this(offset, size, new Sort(direction, properties));
	}

	/**
	 * 
	 * @param offset
	 * @param size
	 * @param sort
	 */
	public OffsetPageRequest(int offset, int size, Sort sort) {

		if (0 > offset) {
			throw new IllegalArgumentException(
					"Page index must not be less than zero!");
		}

		if (0 >= size) {
			throw new IllegalArgumentException(
					"Page size must not be less than or equal to zero!");
		}

		this.offset = offset;
		this.size = size;
		this.sort = sort;
	}

	public int getPageSize() {

		return size;
	}

	public int getPageNumber() {

		return offset / size;
	}

	public int getOffset() {

		return offset;
	}

	public Sort getSort() {

		return sort;
	}

	@Override
	public boolean equals(final Object obj) {

		if (this == obj) {
			return true;
		}

		if (!(obj instanceof OffsetPageRequest)) {
			return false;
		}

		OffsetPageRequest that = (OffsetPageRequest) obj;

		boolean offsetEqual = this.offset == that.offset;
		boolean sizeEqual = this.size == that.size;

		boolean sortEqual = this.sort == null ? that.sort == null : this.sort
				.equals(that.sort);

		return offsetEqual && sizeEqual && sortEqual;
	}

	@Override
	public int hashCode() {

		int result = 17;

		result = 31 * result + offset;
		result = 31 * result + size;
		result = 31 * result + (null == sort ? 0 : sort.hashCode());

		return result;
	}

	@Override
	public Pageable next() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Pageable previousOrFirst() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Pageable first() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasPrevious() {
		// TODO Auto-generated method stub
		return false;
	}
}
