package com.skyapi.weatherforecast.location;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.skyapi.weatherforecast.common.Location;

@Service
public class LocationService {

	private LocationRepository locationrepo;

	public LocationService(LocationRepository locationrepo) {
		super();
		this.locationrepo = locationrepo;
	}
	
	public Location add(Location location)
	{
		return locationrepo.save(location);
	}

	public Location get(String code) 
	{
		return locationrepo.findbyCode(code);
	}
	
	@Deprecated
	List<Location> list() {
		return locationrepo.findUntrashed();
	}
	
	@Deprecated
	public Page<Location> listByPage(int pageNum, int pageSize, String sortField) {
		Sort sort = Sort.by(sortField).ascending();
		Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
		
		return locationrepo.findUntrashed(pageable);
	}
	
	public Page<Location> listByPage(int pageNum, int pageSize, String sortOption, Map<String, Object> filterFields) {
		Sort sort = createMultipleSorts(sortOption);
		
		Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
		
		return locationrepo.listWithFilter(pageable, filterFields);
	}

	private Sort createMultipleSorts(String sortOption) {
		String[] sortFields = sortOption.split(",");
		
		Sort sort = null;
		
		if (sortFields.length > 1) { // sorted by multiple fields
			
			sort = createSingleSort(sortFields[0]);
			
			for (int i = 1; i < sortFields.length; i++) {
				
				sort = sort.and(createSingleSort(sortFields[i]));
			}
			
		} else { // sorted by a single field
			sort = createSingleSort(sortOption);
		}
		return sort;
	}	
	
	private Sort createSingleSort(String fieldName) {
		String actualFieldName = fieldName.replace("-", "");
		return fieldName.startsWith("-")
					? Sort.by(actualFieldName).descending() : Sort.by(actualFieldName).ascending();		
	}
	
	public Location update(Location locationInRequest)
	{
		String code = locationInRequest.getCode();
		
		Location locationINDB = locationrepo.findbyCode(code);
		
		if(locationINDB == null)
		{
			throw new LocationNotFoundException("No Location is found with the given code" + code);
		}
		
		locationINDB.setCityname(locationInRequest.getCityname());
		locationINDB.setRegionname(locationInRequest.getRegionname());
		locationINDB.setCountrycode(locationInRequest.getCountrycode());
		locationINDB.setCountryname(locationInRequest.getCityname());
		locationINDB.setEnabled(locationInRequest.isEnabled());
		
		return locationrepo.save(locationINDB);
	}
	
	public void delete(String code)
	{
		if(!locationrepo.existsById(code))
		{
			throw new LocationNotFoundException("No Location is found with the given code" + code);
		}
		
		locationrepo.trashbyCode(code);
	}
}
