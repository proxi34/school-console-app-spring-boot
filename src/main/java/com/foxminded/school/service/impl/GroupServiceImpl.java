package com.foxminded.school.service.impl;

import com.foxminded.school.dao.GroupDao;
import com.foxminded.school.entity.Group;
import com.foxminded.school.exception.NotFoundException;
import com.foxminded.school.service.GroupService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GroupServiceImpl implements GroupService {
    private final GroupDao groupDao;

    @Autowired
    public GroupServiceImpl(GroupDao groupDao) {
        this.groupDao = groupDao;
    }

    @Override
    public Group getById(int id) {
        return groupDao.get(id)
            .orElseThrow(() -> new NotFoundException(String.format("Group with id %d not found", id)));
    }

    @Override
    public List<Group> getAll() {
        return groupDao.getAll();
    }

    @Override
    public void save(@NonNull Group group) {
        boolean saved = groupDao.save(group);
        if (!saved) {
            throw new IllegalStateException("Cannot save group");
        }
    }

    @Override
    public void update(@NonNull Group group) {
        Optional<Group> groupOptional = groupDao.get(group.getId());
        groupOptional.ifPresentOrElse(presentGroup -> {
            boolean updated = groupDao.update(group);
            if (!updated) {
                throw new IllegalStateException("Cannot update group");
            }
        }, () -> {
            throw new NotFoundException(String.format("Group with id %d not found", group.getId()));
        });
    }

    @Override
    public void deleteById(int id) {
        Optional<Group> groupOptional = groupDao.get(id);
        groupOptional.ifPresentOrElse(group -> {
            boolean deleted = groupDao.delete(id);
            if (!deleted) {
                throw new IllegalStateException(String.format("Cannot delete group with id %d", id));
            }
        }, () -> {
            throw new NotFoundException(String.format("Group with id %d not found", id));
        });
    }

    @Override
    public List<Group> getAllByEqualOrLessStudentsCount(int studentsCount) {
        return groupDao.getAllByEqualOrLessStudentsCount(studentsCount);
    }

    @Override
    public void saveAll(@NonNull List<Group> groups) {
        groups.forEach(group -> save(group));
    }
}
