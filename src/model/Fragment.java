/**
 * 
 *@param
 */
package model;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @param
 */
public class Fragment implements Serializable {

	private Fragment fragment;

	private List<Fragment> fragmentlist;

	public List<Fragment> getFragmentlist() {
		return fragmentlist;
	}

	public void setFragmentlist(List<Fragment> fragmentlist) {
		this.fragmentlist = fragmentlist;
	}

	public Fragment getFragment() {
		return fragment;
	}

	public void setFragment(Fragment fragment) {
		this.fragment = fragment;
	}
}
