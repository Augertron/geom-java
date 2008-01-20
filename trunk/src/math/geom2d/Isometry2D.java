/* File Isometry2D.java 
 *
 * Project : geometry
 *
 * ===========================================
 * 
 * This library is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 2.1 of the License, or (at
 * your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY, without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library. if not, write to :
 * The Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 * Created 29 nov. 07
 */

package math.geom2d;

/**
 * Represents an affine planar transform which preserves length ratios and
 * (unoriented) angles. If the Isometry is direct, it also preserves oriented
 * angles.
 * @author dlegland
 */
public interface Isometry2D extends Similarity2D {

	public abstract boolean isDirect();
}
